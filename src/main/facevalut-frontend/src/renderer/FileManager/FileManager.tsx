import React, {useState} from "react";

function FileManager() {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!selectedFile) {
      alert('Please select a file');
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      const response = await fetch('http://localhost:8080/uploadFile', {
        mode: 'cors',
        method: 'POST',
        body: formData,
      });
      const data = await response.json();
      console.log(data);
    } catch (error) {
      console.error(error);
    }
  };

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSelectedFile(event.target.files?.[0] || null);
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>
        <input type="file" onChange={handleFileChange} />
      </label>

      <button type="submit">Upload</button>
    </form>
  );
}

export default FileManager;
