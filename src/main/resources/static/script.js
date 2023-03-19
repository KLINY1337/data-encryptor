const dropZone = document.getElementById('dropZone');

dropZone.addEventListener('dragover', (event) => {
    event.preventDefault();
    dropZone.classList.add('dragover');
});

dropZone.addEventListener('dragleave', (event) => {
    event.preventDefault();
    dropZone.classList.remove('dragover');
});

dropZone.addEventListener('drop', (event) => {
    event.preventDefault();
    dropZone.classList.remove('dragover');

    const files = event.dataTransfer.files;

    for (let i = 0; i < files.length; i++) {
        console.log(files[i]);
        // code to upload file to server goes here
    }
});