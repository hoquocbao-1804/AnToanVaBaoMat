$(document).ready(function () {
    $(".menu-link").click(function () {
        // Xóa class 'active' khỏi tất cả menu items
        $(".menu-link").removeClass("active");

        // Thêm class 'active' cho phần tử được click
        $(".menu-link").addClass("active");
    });
});


    $(document).ready(function () {
    $('#orderTable').DataTable({
        "paging": true,
        "searching": true,
        "ordering": true,
        "info": true,
        "language": {
            "search": "Tìm kiếm:",
            "lengthMenu": "Hiển thị _MENU_ dòng",
            "info": "Hiển thị _START_ đến _END_ của _TOTAL_",
            "paginate": {
                "first": "Đầu",
                "last": "Cuối",
                "next": "Tiếp",
                "previous": "Trước"
            }
        },
        "destroy": true
    });
});

    function showStatusDropdown(orderId, currentStatus) {
    document.querySelectorAll('.status-dropdown').forEach(dropdown => {
        dropdown.style.display = 'none';
    });
    const dropdown = document.getElementById(`statusDropdown-${orderId}`);
    dropdown.style.display = 'flex';
}

    function updateStatus(orderId) {
    const select = document.getElementById(`statusSelect-${orderId}`);
    const newStatus = select.value;

    fetch('${pageContext.request.contextPath}/admin', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
},
    body: `action=updateStatus&orderId=${orderId}&status=${encodeURIComponent(newStatus)}`
})
    .then(response => {
    if (response.ok) {
    const statusSpan = document.querySelector(`#statusDropdown-${orderId}`).parentElement.querySelector('.status-label');
    statusSpan.textContent = newStatus;
    statusSpan.className = 'status-label ' +
    (newStatus === 'Chờ xác nhận' ? 'pending' :
    newStatus === 'Đang vận chuyển' ? 'in-transit' :
    newStatus === 'Đã hoàn thành' ? 'completed' :
    newStatus === 'Đã hủy' ? 'cancelled' : '');
    document.getElementById(`statusDropdown-${orderId}`).style.display = 'none';
} else {
    alert('Cập nhật trạng thái thất bại!');
}
})
    .catch(error => {
    console.error('Error:', error);
    alert('Đã xảy ra lỗi khi cập nhật trạng thái!');
});
}

    function deleteOrder(orderId) {
    if (confirm('Bạn có chắc muốn xóa đơn hàng này?')) {
    fetch('${pageContext.request.contextPath}/admin', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
},
    body: `action=deleteOrder&orderId=${orderId}`
})
    .then(response => {
    if (response.ok) {
    const row = document.querySelector(`#statusDropdown-${orderId}`).closest('tr');
    row.remove();
} else {
    alert('Xóa đơn hàng thất bại!');
}
})
    .catch(error => {
    console.error('Error:', error);
    alert('Đã xảy ra lỗi khi xóa đơn hàng!');
});
}
}

