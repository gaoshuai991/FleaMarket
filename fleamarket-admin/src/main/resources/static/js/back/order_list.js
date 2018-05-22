var pageNum = 1;
var pageSize = 5;
var pageSizeArray = [1, 3, 5, 8, 10];

$(function () {
    setPageSizeSel(pageSizeArray, pageSize);
    loadData();
    initPageSizeSel();
    initSearchBtn();
});

function loadData() {
    $.get(contextPath + "admin/orders",
        {
            "pageNum": pageNum,
            "pageSize": pageSize,
            "column": $("#basic-search-sel").val(),
            "keyword": $("#basic-search-val").val()
        }, function (data) {
            var $userBasicTBody = $("#user-basic-table").find(">tbody");
            $userBasicTBody.empty();
            for (var x = 0; x < data.list.length; x++) {
                var order = data.list[x];
                var tr = $('<tr>\n' +
                    '         <td>' + order.id + '</td>\n' +
                    '         <td>' + order.treasureId + '</td>\n' +
                    '         <td>' + order.userId + '</td>\n' +
                    '         <td>' + order.note + '</td>\n' +
                    '         <td>' + order.logistics + '</td>\n' +
                    '         <td>' + order.name + '</td>\n' +
                    '         <td>' + order.phone + '</td>\n' +
                    '         <td>' + order.address + '</td>\n' +
                    '         <td>' + order.createTime + '</td>\n' +
                    '    </tr>');
                if (order.status === 0) {
                    tr.append($('<td><button id="edit-user-status-' + order.id + '-0" class="btn btn-sm btn-danger"></i>退货</button></td>'));
                } else if (order.status === 1) {
                    tr.append($('<td><button id="edit-user-status-' + order.id + '-1" class="btn btn-sm btn-warning"></i>待发货</button></td>'));
                } else if (order.status === 2) {
                    tr.append($('<td><button id="edit-user-status-' + order.id + '-1" class="btn btn-sm btn-default">已发货</button></td>'));
                } else if (order.status === 3) {
                    tr.append($('<td><button id="edit-user-status-' + order.id + '-1" class="btn btn-sm btn-success">已收货</button></td>'));
                }
                // tr.append($('<td><a class="btn btn-sm btn-info" href="' + contextPath + 'admin/user/details/' + user.id + '"><i class="fa fa-edit"></i>&nbsp;查看详情</a></td>'));
                $userBasicTBody.append(tr);
            }
            setPage(data.pageNum,data.total, data.pages, "goPage");
        }, "json");
}

function goPage(_pageNum) {
    pageNum = _pageNum;
    loadData();
}

function initPageSizeSel() {
    $("#pageSize-sel").change(function () {
        pageSize = $(this).val();
        goPage(1);
    });
}

function initSearchBtn() {
    $("#user-search-btn").click(function () {
        goPage(1);
    })
}