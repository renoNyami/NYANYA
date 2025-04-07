import React, { useState } from 'react';
import axios from 'axios';
import { API_BLOG, API_REGISTER } from '../config.js';

const Register = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const handleRegister = () => {
    if (password === confirmPassword) {
      const userData = {
        username,
        password
      };
      console.log(userData);
      axios.post(API_REGISTER, userData)
       .then((response) => {
          console.log('Registration successful:', response.data);
        })
       .catch((error) => {
          console.error('Registration failed:', error);
        });
    } else {
      console.error('Passwords do not match');
    }
  };

  return (
    <div>
      <h2>Register</h2>
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <input
        type="password"
        placeholder="Confirm Password"
        value={confirmPassword}
        onChange={(e) => setConfirmPassword(e.target.value)}
      />
      <button onClick={handleRegister}>Register</button>
    </div>
  );
};

export default Register;