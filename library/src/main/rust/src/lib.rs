use chacha20poly1305::{
    aead::{Aead, KeyInit},
    ChaCha20Poly1305, Nonce
};
use jni::JNIEnv;
use jni::objects::{JClass, JString};
use jni::sys::jboolean;
use sha3::{Digest, Sha3_256, Keccak256};

#[no_mangle]
pub extern "system" fn Java_com_zaitxcode_android_security_RustEngine_nativeRustIsLoaded<'a>(
    _env: JNIEnv<'a>,
    _class: JClass<'a>,
) -> jboolean {
    1
}

#[no_mangle]
pub extern "system" fn Java_com_zaitxcode_android_security_RustEngine_nativeRustSha3<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass<'a>,
    input: JString<'a>,
) -> JString<'a> {
    let input_str: String = match env.get_string(&input) {
        Ok(s) => s.into(),
        Err(_) => return env.new_string("").unwrap(),
    };

    let mut hasher = Sha3_256::new();
    hasher.update(input_str.as_bytes());
    let result = hasher.finalize();
    let hex_hash = format!("{:x}", result);

    env.new_string(hex_hash).unwrap_or_else(|_| env.new_string("").unwrap())
}

#[no_mangle]
pub extern "system" fn Java_com_zaitxcode_android_security_RustEngine_nativeRustKeccak256<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass<'a>,
    input: JString<'a>,
) -> JString<'a> {
    let input_str: String = match env.get_string(&input) {
        Ok(s) => s.into(),
        Err(_) => return env.new_string("").unwrap(),
    };

    let mut hasher = Keccak256::new();
    hasher.update(input_str.as_bytes());
    let result = hasher.finalize();
    let hex_hash = format!("{:x}", result);

    env.new_string(hex_hash).unwrap_or_else(|_| env.new_string("").unwrap())
}

#[no_mangle]
pub extern "system" fn Java_com_zaitxcode_android_security_RustEngine_nativeRustChaCha20Encrypt<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass<'a>,
    text: JString<'a>,
    key: JString<'a>,
) -> JString<'a> {
    let plain_text: String = match env.get_string(&text) {
        Ok(s) => s.into(),
        Err(_) => return env.new_string("").unwrap(),
    };
    let secret_key: String = match env.get_string(&key) {
        Ok(s) => s.into(),
        Err(_) => return env.new_string("").unwrap(),
    };

    let mut key_bytes = [0u8; 32];
    let key_src = secret_key.as_bytes();
    let len = key_src.len().min(32);
    key_bytes[..len].copy_from_slice(&key_src[..len]);

    let cipher = ChaCha20Poly1305::new(&key_bytes.into());
    let nonce = Nonce::from_slice(b"unique nonce"); // 12-bytes nonce

    match cipher.encrypt(nonce, plain_text.as_bytes()) {
        Ok(ciphertext) => {
            let hex_out = hex::encode(ciphertext);
            env.new_string(hex_out).unwrap_or_else(|_| env.new_string("").unwrap())
        }
        Err(_) => env.new_string("").unwrap(),
    }
}
