// 分页渲染函数
function renderPagination(pageInfo, callback) {
    const totalPages = Math.ceil(pageInfo.total / pageInfo.size);
    let html = '';
    
    // 上一页
    html += `
        <li class="page-item ${pageInfo.current === 1 ? 'disabled' : ''}">
            <a class="page-link" href="javascript:void(0)" onclick="${callback}(${pageInfo.current - 1})" aria-label="上一页">
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>
    `;
    
    // 页码
    for (let i = 1; i <= totalPages; i++) {
        if (i === pageInfo.current) {
            html += `
                <li class="page-item active">
                    <span class="page-link">${i}</span>
                </li>
            `;
        } else {
            html += `
                <li class="page-item">
                    <a class="page-link" href="javascript:void(0)" onclick="${callback}(${i})">${i}</a>
                </li>
            `;
        }
    }
    
    // 下一页
    html += `
        <li class="page-item ${pageInfo.current === totalPages ? 'disabled' : ''}">
            <a class="page-link" href="javascript:void(0)" onclick="${callback}(${pageInfo.current + 1})" aria-label="下一页">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    `;
    
    $('#pagination').html(html);
}

// 提示框函数
function showSuccess(message) {
    Swal.fire({
        icon: 'success',
        title: '成功',
        text: message,
        timer: 1500,
        showConfirmButton: false
    });
}

function showError(message) {
    Swal.fire({
        icon: 'error',
        title: '错误',
        text: message
    });
}

function showConfirm(message) {
    return confirm(message);
}

// 表单验证函数
function validatePhone(phone) {
    return /^1[3-9]\d{9}$/.test(phone);
}

function validatePassword(password) {
    return password.length >= 6;
}

// AJAX错误处理
function handleAjaxError(xhr, callback) {
    if (xhr.status === 401) {
        location.href = '/login';
    } else if (xhr.status === 403) {
        showError('权限不足');
    } else {
        callback ? callback() : showError('操作失败，请重试');
    }
}

// 退出登录函数
function logout() {
    $.ajax({
        url: '/user/logout',
        type: 'POST',
        success: function(res) {
            if (res.code === 200) {
                location.href = '/login';
            } else {
                showError(res.message || '退出失败');
            }
        },
        error: function(xhr) {
            handleAjaxError(xhr);
        }
    });
} 