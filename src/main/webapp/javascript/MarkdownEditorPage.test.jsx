import React from 'react';
import { render, fireEvent, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import MarkdownEditorPage from './MarkdownEditorPage';

// Mock localStorage for Jest environment
beforeAll(() => {
  const localStorageMock = (function() {
    let store = {};
    return {
      getItem(key) { return store[key] || null; },
      setItem(key, value) { store[key] = value.toString(); },
      clear() { store = {}; },
      removeItem(key) { delete store[key]; }
    };
  })();
  Object.defineProperty(global, 'localStorage', { value: localStorageMock });
});

// Helper to clear localStorage before each test
beforeEach(() => {
  localStorage.clear();
});

describe('MarkdownEditorPage localStorage integration', () => {
  it('should load initial values from localStorage', () => {
    localStorage.setItem('md_text', 'Test markdown');
    localStorage.setItem('md_author', 'Test Author');
    localStorage.setItem('md_subject', 'Test Subject');
    localStorage.setItem('md_title', 'Test Title');
    render(<MarkdownEditorPage />);
    expect(screen.getByPlaceholderText('Author')).toHaveValue('Test Author');
    expect(screen.getByPlaceholderText('Subject')).toHaveValue('Test Subject');
    expect(screen.getByPlaceholderText('Title')).toHaveValue('Test Title');
    // Only textarea for markdown
    const textareas = screen.getAllByRole('textbox');
    expect(textareas[textareas.length - 1]).toHaveValue('Test markdown');
  });

  it('should save markdown and metadata to localStorage on change', async () => {
    render(<MarkdownEditorPage />);
    fireEvent.change(screen.getByPlaceholderText('Author'), { target: { value: 'Jane' } });
    fireEvent.change(screen.getByPlaceholderText('Subject'), { target: { value: 'Doc' } });
    fireEvent.change(screen.getByPlaceholderText('Title'), { target: { value: 'MyTitle' } });
    // Only textarea for markdown
    const textareas = screen.getAllByRole('textbox');
    fireEvent.change(textareas[textareas.length - 1], { target: { value: 'Some markdown' } });
    await waitFor(() => {
      expect(localStorage.getItem('md_author')).toBe('Jane');
      expect(localStorage.getItem('md_subject')).toBe('Doc');
      expect(localStorage.getItem('md_title')).toBe('MyTitle');
      expect(localStorage.getItem('md_text')).toBe('Some markdown');
    });
  });
});
