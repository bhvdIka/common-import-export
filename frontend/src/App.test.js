import { render, screen } from '@testing-library/react';

// Mock the components to avoid dependency issues
jest.mock('./components/ImportExport/ImportExportButton', () => {
  return function MockImportExportButton({ moduleType }) {
    return <div data-testid={`import-export-${moduleType}`}>Import/Export Button for {moduleType}</div>;
  };
});

import App from './App';

test('renders Import/Export Manager title', () => {
  render(<App />);
  const titleElement = screen.getByText(/Import\/Export Manager/i);
  expect(titleElement).toBeInTheDocument();
});

test('renders all module sections', () => {
  render(<App />);
  
  expect(screen.getByText('Camera Module')).toBeInTheDocument();
  expect(screen.getByText('Robot Module')).toBeInTheDocument();
  expect(screen.getByText('Task Module')).toBeInTheDocument();
  expect(screen.getByText('User Module')).toBeInTheDocument();
  expect(screen.getByText('Map Module')).toBeInTheDocument();
});