const fileInput = document.getElementById('file');
const submitButton = document.getElementById('submit');
const hashResult = document.getElementById('result');
function generateChecksum() {
    const myFile = fileInput.files[0]
    myFile.arrayBuffer()
        .then((buf) => {
            alert(new TextDecoder().decode(buf));
        })
}

async function fileHash() {
    const file = fileInput.files[0]
    const arrayBuffer = await file.arrayBuffer();
  
    // Use the subtle crypto API to perform a SHA256 Sum of the file's Array Buffer
    // The resulting hash is stored in an array buffer
    const hashAsArrayBuffer = await crypto.subtle.digest('SHA-256', arrayBuffer);
  
    // To display it as a string we will get the hexadecimal value of each byte of the array buffer
    // This gets us an array where each byte of the array buffer becomes one item in the array
    const uint8ViewOfHash = new Uint8Array(hashAsArrayBuffer);
    // We then convert it to a regular array so we can convert each item to hexadecimal strings
    // Where to characters of 0-9 or a-f represent a number between 0 and 16, containing 4 bits of information, so 2 of them is 8 bits (1 byte).
    const hashAsString = Array.from(uint8ViewOfHash).map((b) => b.toString(16).padStart(2, '0')).join('');
    alert(hashAsString);
  }

submitButton.onclick = fileHash
