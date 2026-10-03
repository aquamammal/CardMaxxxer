package com.cardmaxxxer.core.security

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Manages the SQLCipher database passphrase using Android Keystore.
 * Generates a random 32-byte passphrase, encrypts it with an AES/GCM key
 * stored in the Android Keystore, and persists the encrypted blob in SharedPreferences.
 */
class DatabaseKeyManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

    /**
     * Returns the raw 32-byte passphrase for SQLCipher.
     * Decrypts the stored blob using the Keystore-backed key.
     */
    @Synchronized
    fun getPassphrase(): ByteArray {
        val encrypted = prefs.getString(KEY_ENCRYPTED_PASSPHRASE, null)
        val iv = prefs.getString(KEY_IV, null)

        if (encrypted != null && iv != null) {
            return decrypt(android.util.Base64.decode(encrypted, android.util.Base64.NO_WRAP),
                android.util.Base64.decode(iv, android.util.Base64.NO_WRAP))
        }

        // First launch: generate, encrypt, store
        val passphrase = generatePassphrase()
        val (encBlob, ivBytes) = encrypt(passphrase)
        prefs.edit()
            .putString(KEY_ENCRYPTED_PASSPHRASE, android.util.Base64.encodeToString(encBlob, android.util.Base64.NO_WRAP))
            .putString(KEY_IV, android.util.Base64.encodeToString(ivBytes, android.util.Base64.NO_WRAP))
            .apply()
        return passphrase
    }

    private fun generatePassphrase(): ByteArray {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        return bytes
    }

    private fun getOrCreateKey(): SecretKey {
        val entry = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return entry?.secretKey ?: generateKey()
    }

    private fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()
        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    private fun encrypt(plain: ByteArray): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(plain)
        return Pair(encrypted, iv)
    }

    private fun decrypt(encrypted: ByteArray, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), spec)
        return cipher.doFinal(encrypted)
    }

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "cardmaxxxer_db_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val PREFS_NAME = "cardmaxxxer_secure_prefs"
        private const val KEY_ENCRYPTED_PASSPHRASE = "encrypted_passphrase"
        private const val KEY_IV = "passphrase_iv"
    }
}
