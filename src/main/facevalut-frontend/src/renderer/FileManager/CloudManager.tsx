import React, { useState } from 'react';
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


  return (
    <div className="MainApp">
      <div className="leftsection">
        <div className="leftsection__header">
          <div className="iconcircle"></div>
          <h1>header1</h1>
        </div>

        <div className="leftsection__body">
        </div>

      </div>

      <div className="rightsection">
        <div className="rightsection__header">
          <h1>All Files</h1>
          <button onClick={handleButtonClick}>Upload</button>
          <Popup isOpen={popupOpen} onClose={handleClosePopup} />
        </div>

        <div className="rightsection__body">
          <h1>FILE  showcase</h1>
        </div>

      </div>
    </div>
  );

}

export default CloudManager;
