# Enigma Machine Simulator (Java Swing)

A desktop application that recreates the encryption and decryption process of the historical Enigma machine. This project features a graphical user interface built with Java Swing, allowing for a tactile and interactive experience with WWII-era cryptography.

## 🚀 Features

* **Interactive GUI:** A visual representation of the lampboard, rotors, and keyboard.
* **Authentic Logic:** Implements the stepping mechanism (including double-stepping) of the Enigma I and M3 models.
* **Full Configuration:**
    * **Rotor Selection:** Choose and order rotors (I, II, III, IV, V).
    * **Ring Settings (Ringstellung):** Configure internal wiring offsets.
    * **Start Positions (Grundstellung):** Set the initial rotor alignment.
    * **Plugboard (Steckerbrett):** Define letter swaps for enhanced security.
* **Real-time Processing:** Encrypts or decrypts as you type.

---

## 🛠 Installation & Getting Started

### Prerequisites
* **Java Development Kit (JDK) 11** or higher.
* An IDE (like IntelliJ IDEA, Eclipse, or VS Code) or Maven.

### Running the App
1. **Clone the repository:**
```bash
   git clone [https://github.com/yourusername/enigma-swing-gui.git](https://github.com/yourusername/enigma-swing-gui.git)
````

2.  **Navigate to the directory:**
    ```bash
    cd enigma-swing-gui
    ```
3.  **Compile and Run:**
    ```bash
    javac -d bin src/*.java
    java -cp bin Main
    ```

-----

## 🧩 Cryptographic Logic

The Enigma machine is a polyalphabetic substitution cipher. The security of the system relies on the signal path through the components.

The transformation $E$ for a single letter can be mathematically represented as:

$$E = P \cdot R_1 \cdot R_2 \cdot R_3 \cdot U \cdot R_3^{-1} \cdot R_2^{-1} \cdot R_1^{-1} \cdot P^{-1}$$

  * **P**: Plugboard
  * **R**: Rotors (Right, Middle, Left)
  * **U**: Reflector (Umkehrwalze)

-----

## 📖 Usage

1.  **Set the Key:** Configure the Rotors and Ring Settings to your desired "day key."
2.  **Plugboard:** Map your letter pairs (e.g., `AB`, `EZ`).
3.  **Input:** Type your message into the input field.
4.  **Decrypt:** To decrypt, reset the rotors to the **exact same starting positions** used for encryption and type the ciphertext back in.

-----
