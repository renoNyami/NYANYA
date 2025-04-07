import React, { useState, useEffect } from'react';
import axios from 'axios';
import { API_PUBLISH, API_BLOG } from '../config.js';
import { message } from 'antd'; // 假设使用antd组件库来展示提示信息，你可根据实际情况调整

const PublishBlog = () => {
    // 使用useState管理各个状态
    const [blogs, setBlogs] = useState([]);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [showNoDataMessage, setShowNoDataMessage] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [editingBlog, setEditingBlog] = useState(null);
    const [isAddModalVisible, setIsAddModalVisible] = useState(false);
    const [newBlog, setNewBlog] = useState({
        title: '',
        content: '',
        userId: '5'
    });
    const [currentBlogId, setCurrentBlogId] = useState(null);
    const [currentBlogContent, setCurrentBlogContent] = useState('');
    const [currentBlogTitle, setCurrentBlogTitle] = useState('');
    const [currentBlogUserId, setCurrentBlogUserId] = useState('');
    const [currentBlogPostTime, setCurrentBlogPostTime] = useState('');
    const [searchKeyword, setSearchKeyword] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [isSearching, setIsSearching] = useState(false);
    const [showDetailButton, setShowDetailButton] = useState(true);

    // 模拟从后端获取用户信息的函数，你可根据实际情况替换为真实的获取用户信息逻辑
    const getUserId = () => {
        // 这里假设返回一个固定的用户ID示例，实际中可能是从登录状态等获取
        return '123';
    };

    // 获取博客列表的函数，在组件挂载时调用，也可在其他需要刷新列表的场景调用
    const fetchBlogs = () => {
        setIsLoading(true);
        axios.get(API_BLOG)
          .then(response => {
                if (response.data.length === 0) {
                    setShowNoDataMessage(true);
                } else {
                    const adaptedData = response.data.map(blog => ({
                      ...blog,
                        blogId: blog.blogId
                    }));
                    setBlogs(adaptedData);
                    setSearchResults(adaptedData); // 初始时让搜索结果等同于所有博客数据
                }
            })
          .catch(error => {
                if (error.response) {
                    console.error("There was an error fetching the blogs! Status code: ", error.response.status);
                    setError(`Error fetching blogs: ${error.response.statusText}`);
                } else if (error.request) {
                    console.error("There was an error fetching the blogs! Request failed.");
                    setError("Error fetching blogs: Request failed.");
                } else {
                    console.error("There was an error fetching the blogs! Unknown error.");
                    setError("Error fetching blogs: Unknown error.");
                }
            })
          .finally(() => {
                setIsLoading(false);
            });
    };

    useEffect(() => {
        fetchBlogs();
    }, []); // 在组件挂载时获取博客列表

    // 新增博客的函数，在新增博客modal中点击确认添加后调用
    const addBlog = async () => {
        const { title, content, userId } = newBlog;
        if (!title ||!content ||!userId) {
            message.error('请填写完整的博客信息');
            return;
        }
        try {
            const response = await axios.post(`${API_PUBLISH}`, {
                userId: userId || getUserId(), // 如果没填userId则获取默认的用户ID
                title,
                content
            });
            if (response.status === 200) {
                message.success('博客新增成功');
                setIsAddModalVisible(false);
                setNewBlog({
                    title: '',
                    content: '',
                    userId: ''
                });
                fetchBlogs(); 
            } else {
                message.error('新增博客失败，服务器返回异常状态码');
            }
        } catch (error) {
            message.error('新增博客失败，请稍后重试');
            console.error(error);
        }
    };

    const handleAddCancel = () => {
        setIsAddModalVisible(false);
        setNewBlog({
            title: '',
            content: '',
            userId: ''
        });
    };

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
            {error && <p style={{ color:'red' }}>{error}</p>}
            {isLoading? (
                <p>正在加载博客数据，请稍候...</p>
            ) : showNoDataMessage? (
                <p>暂无博客数据</p>
            ) : (
                <>
                    <input
                        type="text"
                        placeholder="请输入博客标题"
                        value={newBlog.title}
                        onChange={(e) => setNewBlog({...newBlog, title: e.target.value })}
                    />
                    <textarea
                        placeholder="请输入博客正文"
                        value={newBlog.content}
                        onChange={(e) => setNewBlog({...newBlog, content: e.target.value })}
                    />
                    <button onClick={addBlog}>发布博客</button>

                    <input
                        type="text"
                        placeholder="搜索博客"
                        value={searchKeyword}
                        onChange={(e) => setSearchKeyword(e.target.value)}
                    />
                    <button onClick={handleSearch}>搜索</button>

                    {searchResults.map(blog => (
                        <div key={blog.blogId}>
                            <h3>{blog.title}</h3>
                            <p>{blog.content}</p>
                        </div>
                    ))}
                </>
            )}
        </div>
    );
};

export default PublishBlog;