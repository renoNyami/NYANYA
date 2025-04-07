import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { API_BLOG } from '../config.js';
import { Link } from'react-router-dom';
import Delete from './Delete';

const BlogList = () => {
  const [blogs, setBlogs] = useState([]);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [isSearching, setIsSearching] = useState(false);

  useEffect(() => {
    axios.get(API_BLOG)
    .then((response) => {
        console.log('Received data:', response.data);
        setBlogs(response.data);
        setSearchResults(response.data);
      })
    .catch((error) => {
        console.error('Error fetching blogs:', error);
      });
  }, []);

  const handleSearch = () => {
    setIsSearching(true);
    const filteredResults = blogs.filter(blog =>
      blog.title.toLowerCase().includes(searchKeyword.toLowerCase())
    );
    setSearchResults(filteredResults);
    setIsSearching(false);
  };

  return (
    <div>
      <h2>Blogs</h2>
      <input
        type="text"
        placeholder="搜索博客"
        value={searchKeyword}
        onChange={(e) => setSearchKeyword(e.target.value)}
      />
      <button onClick={handleSearch}>搜索</button>
      {isSearching? (
        <p>正在搜索博客，请稍候...</p>
      ) : searchResults.length === 0? (
        <p>没有找到匹配的博客</p>
      ) : (
        searchResults.map((blog) => (
          <div key={blog.blogId}>
            <h3>{blog.title}</h3>
            <p>{blog.content}</p>
            <p>Posted at: {blog.postTime}</p>
            <Delete blogId={blog.blogId} />
          </div>
        ))
      )}
      <Link to="/publish">
        <button>发布博客</button>
      </Link>
    </div>
  );
};

export default BlogList;