import React, {useState} from "react";

interface File {
  name: string;
  size: number;
  type: string;
}

const FileList = ({ files }: { files: File[] }) => (
  <ul>
    {files.map((file) => (
      <li key={file.name}>
        {file.name} ({file.size} bytes) - {file.type}
      </li>
    ))}
  </ul>
);

const FileUploader = ({ onUpload }: { onUpload: (files: File[]) => void }) => {
  const [selectedFiles, setSelectedFiles] = useState<File[]>([]);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const fileList = e.target.files;
    if (fileList) {
      const files: File[] = [];
      for (let i = 0; i < fileList.length; i++) {
        const file = fileList[i];
        files.push({
          name: file.name,
          size: file.size,
          type: file.type,
        });
      }
      setSelectedFiles(files);
      onUpload(files);
    }
  };

  return (
    <div>
      <input type="file" multiple onChange={handleFileChange} />
      <FileList files={selectedFiles} />
    </div>
  );
};



const FileManager = () => {
  const [files, setFiles] = useState<File[]>([]);

  const handleUpload = (uploadedFiles: File[]) => {
    setFiles((prevFiles) => [...prevFiles, ...uploadedFiles]);
  };

  const handleSendFiles = async () => {
    try {
      const response = await fetch("/api/upload", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(files),
      });
      const data = await response.json();
      console.log(data);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div>
      <FileUploader onUpload={handleUpload} />
      <button onClick={handleSendFiles}>Send Files</button>
      {/*<FileList files={files} />*/}
    </div>
  );
};

export default FileManager;
