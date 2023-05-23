import React, { useState } from 'react';
import EncodedFileList from 'renderer/EncodedFileList';
import './ManagerStyle.css';
import Popup from "./UploadPopup";





function CloudManager(){
  const [popupOpen, setPopupOpen] = useState(false);

  const handleButtonClick = () => {
    setPopupOpen(true);
  };

  const handleClosePopup = () => {
    setPopupOpen(false);
  };

  // @ts-ignore
  const handleDownload = async (event) => {
    event.preventDefault();
    console.log(event.target.text.value);
    try {
      const msg = "name="+encodeURIComponent(event.target.text.value)
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

  return (
    <div className="MainApp">
      <div className="leftsection">
        <div className="leftsection__header">
          <div className="iconcircle"></div>
          <h1>User</h1>
        </div>

        <div className="leftsection__body">
          <p>Your total uploaded files: 5</p>
        </div>

      </div>

      <div className="rightsection">
        <div className="rightsection__header">
          <h1 className="filesHeader">All Files</h1>
          <button className="uploadButton" onClick={handleButtonClick}>Upload</button>
          <Popup isOpen={popupOpen} onClose={handleClosePopup} />
        </div>

        <div className="rightsection__body">

          <EncodedFileList/>

          <h2 className="downloadHeader">Download section</h2>
          <p>Enter filename from section above and submit</p>
          <form onSubmit={handleDownload} className="downloadForm">
            <input type="text" name="text" />
            <input type="submit" value="Submit" />

          </form>
        </div>

      </div>
    </div>
  );

}

export default CloudManager;
