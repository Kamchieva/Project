# JavaScript Form Submission Explained

This document breaks down the JavaScript code used in `register.html` to handle form submissions to a JSON-based API.

## The Goal

The primary goal of this script is to solve a common web development problem: **sending data from a standard HTML form to a modern JSON-based API.**

A regular HTML form sends data in a format called `application/x-www-form-urlencoded`, but a Spring Boot API endpoint with `@RequestBody` expects `application/json`. This script acts as a bridge, intercepting the form submission and converting the data to the correct format before sending it.

---

## Code Breakdown

1.  **`document.getElementById('registration-form').addEventListener('submit', ...)`**
    *   This line finds the HTML `<form>` element with the ID `registration-form` and attaches an "event listener" to it.
    *   The listener waits for the `submit` event, which happens when the user clicks the "Register" button. When that event occurs, the browser is told to run the function provided.

2.  **`event.preventDefault();`**
    *   This is the **most important** first step. It immediately stops the browser's default behavior, which would be to reload the page and send the form data in the wrong format. This allows our script to take full control of the submission process.

3.  **`const formData = new FormData(form);`**
    *   This creates a `FormData` object, which is a convenient way to capture all the data from the form's input fields (e.g., the username and password the user typed in).

4.  **`const data = Object.fromEntries(formData.entries());`**
    *   This is the **data conversion** step. It takes the form data and turns it into a clean JavaScript object. For example:
    ```javascript
    {
      "username": "someuser",
      "password": "somepassword"
    }
    ```

5.  **`const response = await fetch(form.action, { ... });`**
    *   This uses the browser's modern `fetch` API to send the data to the server.
    *   `form.action`: It gets the URL from the form's `action` attribute (which is `/api/auth/register`).
    *   `method: 'POST'`: It specifies that this is a `POST` request.
    *   `headers: { 'Content-Type': 'application/json' }`: This header is critical. It tells your Spring Boot backend that the data being sent is in JSON format, which allows the `@RequestBody` annotation to work correctly.
    *   `body: JSON.stringify(data)`: This takes the JavaScript object from step 4 and converts it into a JSON string, which is then sent as the body of the request.

6.  **`if (response.ok) { ... } else { ... }`**
    *   This block checks if the request was successful. `response.ok` is true if the server returns a success status code (like 200 or 201).
    *   **On Success:**
        *   It displays a green "Registration successful!" message to the user.
        *   It then waits for 2 seconds (`setTimeout`) before automatically redirecting the user to the login page (`window.location.href = '/login'`). This provides a smooth and clear user experience.
    *   **On Failure:**
        *   It displays a red "Registration failed" message, including any error details sent back from the server.

7.  **`try { ... } catch (error) { ... }`**
    *   This is an error-handling block. If the `fetch` request fails for any reason (e.g., a network error, or the server is down), the `catch` block will execute, displaying a generic error message to the user and logging the technical details to the browser's console for debugging.
