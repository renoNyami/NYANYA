import React, { useState } from'react';
import axios from 'axios';
import { API_LOGIN } from '../config.js';
import Register from '../components/Register';


const Login = ({ setIsLoggedIn }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const handleLogin = async () => {
    // 验证输入是否为空
    if (!username || !password) {
      setErrorMessage('用户名和密码不能为空');
      return;
    }
    console.log('准备发送的用户名:', username);
    console.log('准备发送的密码:', password);
    try {
      // 发送POST请求到后端登录接口（这里假设后端接口地址是 /login，和你定义的Servlet路径对应）
      const response = await axios.post(API_LOGIN, {
        username,
        password
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      console.log(response);
      if (response.data.split("!!")[0] === "登陆成功") {
        localStorage.setItem('username', username);
        localStorage.setItem('userId', parseInt(response.data.charAt(response.data.length - 1)));
        console.log(response.data.length - 1)
        window.location.href = '/bloglist';
      } else {
        setErrorMessage("用户名或密码不正确!");
      }
    } catch (error) {
      if (error.response && error.response.data) {
        setErrorMessage(error.response.data);
      } else {
        setErrorMessage('登录出现未知错误，请稍后再试');
      }
    }
  };

  return (
    <div>
      <h2>登录</h2>
      {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
      <input
        type="text"
        placeholder="请输入用户名"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <input
        type="password"
        placeholder="请输入密码"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button onClick={handleLogin}>登录</button>
      <Register />
    </div>
  );
};

export default Login;