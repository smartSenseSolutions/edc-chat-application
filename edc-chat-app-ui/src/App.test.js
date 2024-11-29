/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

import { render, screen } from "@testing-library/react";
import App from "./App";

test("renders learn react link", () => {
    render(<App />);
    const linkElement = screen.getByText(/learn react/i);
    expect(linkElement).toBeInTheDocument();
});
