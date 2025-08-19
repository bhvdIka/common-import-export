import React from 'react';
import ImportExportButton from './components/ImportExport/ImportExportButton';
import './styles/global.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>Import/Export Manager</h1>
        <p>Common Import/Export Data functionality for multiple modules</p>
      </header>
      <main className="App-main">
        <div className="module-section">
          <h2>Camera Module</h2>
          <ImportExportButton moduleType="camera" />
        </div>
        <div className="module-section">
          <h2>Robot Module</h2>
          <ImportExportButton moduleType="robot" />
        </div>
        <div className="module-section">
          <h2>Task Module</h2>
          <ImportExportButton moduleType="task" />
        </div>
        <div className="module-section">
          <h2>User Module</h2>
          <ImportExportButton moduleType="user" />
        </div>
        <div className="module-section">
          <h2>Map Module</h2>
          <ImportExportButton moduleType="map" />
        </div>
      </main>
    </div>
  );
}

export default App;