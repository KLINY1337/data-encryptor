import { MemoryRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import CloudManager from "./FileManager/CloudManager";

function Hello() {

  return (
    <div>
      {/*<Uploader />*/}
      {/*<DisplayResponse />*/}
      <CloudManager />
    </div>
  );
}

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Hello />} />
      </Routes>
    </Router>
  );
}
