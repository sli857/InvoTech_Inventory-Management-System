import {test, describe, expect} from 'vitest';
import {render, screen} from '@testing-library/react';
import IMSShipments from '../../components/content/IMSShipments';

/**
 * Test suite for the IMSShipments component
 */
describe('IMSShipments visual', () => {
  /**
     * Test if the table is rendered correctly
     */
  test('should show table', () => {
    render(<IMSShipments/>);

    expect(screen.getByText('ID')).toBeTruthy();
    expect(screen.getByText('Source')).toBeTruthy();
    expect(screen.getByText('Destination')).toBeTruthy();
    expect(screen.getByText('Current Location')).toBeTruthy();
    expect(screen.getByText('Departed')).toBeTruthy();
    expect(screen.getByText('Estimated Arrival')).toBeTruthy();
    expect(screen.getByText('Actual Arrival')).toBeTruthy();
    expect(screen.getByText('Status')).toBeTruthy();

    // Table should be empty in test
    expect(screen.getByText('0 Shipments In Table')).toBeTruthy();
  });

  /**
     * Test if the form is rendered correctly
     */
  test('should show form', () => {
    render(<IMSShipments/>);

    expect(screen.getByText('From Site')).toBeTruthy();
    expect(screen.getByText('To Site')).toBeTruthy();
    expect(screen.getByText('Item')).toBeTruthy();
    expect(screen.getByText('Quantity')).toBeTruthy();
    expect(screen.getByText('Submit')).toBeTruthy();
  });

  /**
     * Test if the filter is rendered correctly
     */
  test('should show filter', () => {
    render(<IMSShipments/>);

    expect(screen.getByText('Filters')).toBeTruthy();
    expect(screen.getByText('Reset Filters')).toBeTruthy();
  });
});
