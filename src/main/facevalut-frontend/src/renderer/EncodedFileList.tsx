import React, { useState, useEffect } from 'react';

const handleDownload = async (event) => {
    // event.preventDefault();
    try {
      const msg = "name="+encoded
      const response = await fetch('http://localhost:8080/downloadFile?'+msg, {
        mode: 'cors',
        method: 'POST',
        // body: JSON.stringify({"name":event.target.text.value}),
      });
      const data = await response.json();
      console.log(data);
    }
    catch (error) {
    console.error(error);
    }

};

interface ResponseData {
    data: JSON;
}

const EncodedFileList = () => {

    const [responseData, setResponseData] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
          try {
            const response = await fetch('http://localhost:8080/getLinkList/mock', { mode: 'cors' });
            if (!response.ok) {
              throw new Error('Failed to fetch data');
            }
            const data = await response.json();
            setResponseData(data);
          } catch (error) {
            console.error(error);
          }
        };
    
        fetchData();
      }, []);
    


    return (
        <div>
        {responseData ? (
          <ul>
            {responseData.map((item) => (
              <li>{item.decryptedFileName} {item.encryptedFileName} <button onClick={handleDownload}>Download</button></li>
            ))}
          </ul>
        ) : (
          <div>Loading...</div>
        )}
      </div>
    );
  };
  
  export default EncodedFileList;