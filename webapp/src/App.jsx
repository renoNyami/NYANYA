import React from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import BlogList from './components/bloglist';
import Login from './components/Login';
import Register from './components/Register';
import PublishBlog from './components/PublishBlog';

function App() {
  return (
    <div className="App">
      <header>
        <h1>Blog Application</h1>
      </header>
      <Router>
        <Routes>
          <Route path="/" element={<Login />} />
          {/* <Route path="/" element={<Register />} /> */}
          <Route path="/bloglist" element={<BlogList/>} />
          <Route path="/publish" element={<PublishBlog />} /> 
        </Routes>
      </Router>
    </div>
  );
}

export default App;