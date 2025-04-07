import React, { useState } from'react';
import axios from 'axios';
import { Button, message } from 'antd';
import { API_DELETE } from '../config';

const DeleteBlog = ({ blogId }) => {
    // 定义状态来表示是否正在删除博客以及删除操作的结果提示信息
    const [isDeleting, setIsDeleting] = useState(false);
    const [deleteResultMessage, setDeleteResultMessage] = useState('');

    // 处理删除博客的函数
    const handleDelete = async () => {
        setIsDeleting(true);
        try {
            // 使用API_DELETE配置项拼接正确的请求地址
            const response = await axios.post(`${API_DELETE}?blogId=${blogId}`);
            if (response.data.message === "博客删除成功") {
                setDeleteResultMessage('博客删除成功');
                message.success('博客已成功删除');
            } else {
                setDeleteResultMessage(response.data.message);
                message.error('博客删除失败：' + response.data.message);
            }
        } catch (error) {
            setDeleteResultMessage('删除博客出现错误，请稍后再试');
            message.error('删除博客出现错误，请稍后再试');
            console.error(error);
        } finally {
            setIsDeleting(false);
        }
    };

    return (
        <div>
            {isDeleting? (
                <p>正在删除博客，请稍候...</p>
            ) : (
                <Button type="danger" onClick={handleDelete} disabled={isDeleting}>
                    删除博客
                </Button>
            )}
            {deleteResultMessage && <p style={{ color: deleteResultMessage.includes('成功')?'green' :'red' }}>{deleteResultMessage}</p>}
        </div>
    );
};

export default DeleteBlog;