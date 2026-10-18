use jni::JNIEnv;
use jni::objects::{JClass, JString};
use jni::sys::jboolean;
use sha3::{Digest, Sha3_256};

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
