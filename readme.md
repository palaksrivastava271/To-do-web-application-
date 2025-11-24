# To Do List Web Application

A lightweight, self-contained web-based to-do list application implemented in pure Java, without the use of third-party frameworks.

## Overview

This project demonstrates how to create a fully functional web application using the Java Standard Library. This project will use `com.sun.net.httpserver.HttpServer` to serve up HTTP requests with no need for frameworks like Spring Boot or application servers like Tomcat.

## Features

- ✅ Add new tasks
- ✅ Mark tasks as complete
- ✅ Delete tasks
- ✅ Clean, human-centered UI design
- ✅ No external dependencies
- ✅ Single-file implementation

## Prerequisites

- ✅ Java Development Kit (JDK) 8 or higher
- ✅ A web browser

## Installation & Setup

### Clone the repository

```bash
[git clone https://github.com/yourusername/todo-list-webapp.git](https://github.com/rushindra1404/java_to_do_list.git)
```

### Compile the application

```bash
javac ToDoListWebApp.java
```

### Run the application

```bash
java ToDoListWebApp
```

### Enter the application

Open your web browser and go to:

```
http://localhost:8080
```

## Usage

### Adding a Task

1. Type your task description in the input field
2. Click the "Add Task" button
3. Your assignment will show up in the list below.

### Performing a Task
Click the "✓" button next to a task to mark it as complete. Completed tasks will be visually different (usually with a strike through).

### Deleting a Task
Click the "✗" button next to a task to remove it from the list.

## Limitations
- Data is kept in memory and will be lost when the server stops.
- No authentication or user management.
- Single-user design - shared state across all clients.
- Basic styling with embedded CSS.
