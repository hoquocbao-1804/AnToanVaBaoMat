
    // Lấy contextPath từ JSP để fetch hoạt động đúng nếu dùng file .js ngoài
    const contextPath = '${pageContext.request.contextPath}';

    $(document).ready(function () {
    // Xử lý highlight cho menu
    $(".menu-link").click(function () {
        $(".menu-link").removeClass("active");
        $(this).addClass("active");
    });

    // Khởi tạo DataTable
    $('#orderTable').DataTable({
    paging: true,
    searching: true,
    ordering: true,
    info: true,
    language: {
    search: "Tìm kiếm:",
    lengthMenu: "Hiển thị _MENU_ dòng",
    info: "Hiển thị _START_ đến _END_ của _TOTAL_ đơn hàng",
    paginate: {
    first: "Đầu",
    last: "Cuối",
    next: "Tiếp",
    previous: "Trước"
}
},
    destroy: true
});
});

    // Hiển thị dropdown thay đổi trạng thái
    function showStatusDropdown(orderId) {
    document.querySelectorAll('.status-dropdown').forEach(dropdown => {
        dropdown.style.display = 'none';
    });
    const dropdown = document.getElementById(`statusDropdown-${orderId}`);
    if (dropdown) {
    dropdown.style.display = 'flex';
}
}

    // Cập nhật trạng thái đơn hàng
    function updateStatus(orderId) {
    const select = document.getElementById(`statusSelect-${orderId}`);
    const newStatus = select.value;

    fetch(`${contextPath}/admin`, {
    method: 'POST',
    headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
},
    body: `action=updateStatus&orderId=${orderId}&status=${encodeURIComponent(newStatus)}`
})
    .then(response => {
    if (response.ok) {
    // Cập nhật giao diện
    const statusLabel = document.querySelector(`#statusDropdown-${orderId}`).parentElement.querySelector('.status-label');
    statusLabel.textContent = newStatus;
    statusLabel.className = 'status-label ' + getStatusClass(newStatus);
    document.getElementById(`statusDropdown-${orderId}`).style.display = 'none';
    alert('Cập nhật trạng thái thành công!');
} else {
    alert('Cập nhật trạng thái thất bại!');
}
})
    .catch(error => {
    console.error('Lỗi khi cập nhật trạng thái:', error);
    alert('Đã xảy ra lỗi khi cập nhật trạng thái!');
});
}

    // Xóa đơn hàng
    function deleteOrder(orderId) {
    if (confirm('Bạn có chắc chắn muốn xóa đơn hàng này?')) {
    fetch(`${contextPath}/admin`, {
    method: 'POST',
    headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
},
    body: `action=deleteOrder&orderId=${orderId}`
})
    .then(response => {
    if (response.ok) {
    // Xóa dòng đơn hàng khỏi bảng
    const row = document.getElementById(`statusDropdown-${orderId}`).closest('tr');
    if (row) row.remove();
    alert('Đơn hàng đã được xóa thành công!');
} else {
    alert('Xóa đơn hàng thất bại!');
}
})
    .catch(error => {
    console.error('Lỗi khi xóa đơn hàng:', error);
    alert('Đã xảy ra lỗi khi xóa đơn hàng!');
});
}
}

    // Hàm phụ trợ: lấy class theo trạng thái
    function getStatusClass(status) {
    switch (status) {
    case 'Chờ xác nhận': return 'pending';
    case 'Đang vận chuyển': return 'in-transit';
    case 'Đã hoàn thành': return 'completed';
    case 'Đã hủy': return 'cancelled';
    default: return '';
}
}

