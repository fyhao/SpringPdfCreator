import React from 'react';
import { fireEvent, render, screen } from '@testing-library/react';
import ListView from './ListView';

test('clickable cells call handleGridCellClick with row context', () => {
  const handler = jest.fn();
  const row = { id: 7, name: 'Report' };
  render(<ListView options={{
    fields: [{ key: 'name', heading: 'Name', clickable: true }],
    data: [row],
    handleGridCellClick: handler
  }} />);
  fireEvent.click(screen.getByRole('button', { name: 'Report' }));
  expect(handler).toHaveBeenCalledWith(row, 'name', 'Report');
});
