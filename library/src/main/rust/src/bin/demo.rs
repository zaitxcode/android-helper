use sha3::{Digest, Sha3_256};

fn main() {
    println!("🦀 Android Helper - Native Rust Engine Interactive Demo");
    println!("---------------------------------------------------");

    let input = "Hello Rust Engine in Android Helper 1.1.0-alpha01";
    println!("Input Text: {}", input);

    let mut hasher = Sha3_256::new();
    hasher.update(input.as_bytes());
    let result = hasher.finalize();

    println!("SHA3-256 Hash: {:x}", result);
    println!("Status: Rust Engine C-ABI Native Module Operational ⚡");
}
