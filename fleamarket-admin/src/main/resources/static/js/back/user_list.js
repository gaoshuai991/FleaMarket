var pageNum = 1;
var pageSize = 5;
var pageSizeArray = [1, 3, 5, 8, 10];

$(function () {
    setPageSizeSel(pageSizeArray, pageSize);
    loadData();
    initPageSizeSel();
    initIdentityBtn();
    initSearchBtn();
});

function loadData() {
    $.get(contextPath + "admin/user/basic",
        {
            "pageNum": pageNum,
            "pageSize": pageSize,
            "column": $("#basic-search-sel").val(),
            "keyword": $("#basic-search-val").val()
        }, function (data) {
            var $userBasicTBody = $("#user-basic-table").find(">tbody");
            $userBasicTBody.empty();
            for (var x = 0; x < data.list.length; x++) {
                var user = data.list[x];
                var tr = $('<tr>\n' +
                    '         <td><img src="' + contextPath + 'file?path=' + user.photo + '" class="photo"></td>\n' +
                    '         <td>' + user.id + '</td>\n' +
                    '         <td>' + user.username + '</td>\n' +
                    '         <td>' + user.nickname + '</td>\n' +
                    '         <td>' + user.sex + '</td>\n' +
                    '         <td>' + user.tel + '</td>\n' +
                    '         <td>' + user.email + '</td>\n' +
                    '         <td>' + user.birthday + '</td>\n' +
                    '         <td>' + user.city + '</td>\n' +
                    '         <td>' + user.school + '</td>\n' +
                    '         <td>' + user.createTime + '</td>\n' +
                    '    </tr>');
                if (user.status === 0) {
                    tr.append($('<td><button id="edit-user-status-' + user.id + '-0" class="btn btn-sm btn-danger"><i class="fa fa-times"></i>&nbsp;已锁定</button></td>'));
                } else if (user.status === 1) {
                    tr.append($('<td><button id="edit-user-status-' + user.id + '-1" class="btn btn-sm btn-success"><i class="fa fa-check"></i>&nbsp;已激活</button></td>'));
                }
                // tr.append($('<td><a class="btn btn-sm btn-info" href="' + contextPath + 'admin/user/details/' + user.id + '"><i class="fa fa-edit"></i>&nbsp;查看详情</a></td>'));
                $userBasicTBody.append(tr);
            }
            setPage(data.pageNum,data.total, data.pages, "goPage");
            initEditUserStatusBtn();
        }, "json");
}

function goPage(_pageNum) {
    pageNum = _pageNum;
    loadData();
}

function initEditUserStatusBtn() {
    $("button[id^='edit-user-status']").click(function () {
        var userId = this.id.split("-")[3];
        var status = this.id.split("-")[4];
        var text = status === "1" ? "锁定ID为" + userId + "的用户？" : "激活ID为" + userId + "的用户？";
        var $btn = $(this);
        swal({
            title: text,
            icon: "warning",
            buttons: ["取消", "确定"]
        }).then(result => {
            if (result) {
                $.ajax({
                    url: contextPath + "admin/user/basic/" + userId,
                    type: "put",
                    data: {"status": status ^ 1},
                    dataType: "text",
                    success: function (data) {
                        if (data === "true") {
                            swal("账号状态修改成功！", "", "success");
                            if (status === "1") {
                                $btn.removeClass("btn-success");
                                $btn.addClass("btn-danger");
                                $btn.html('<i class="fa fa-times"></i>&nbsp;已锁定');
                                $btn.attr("id", "edit-user-status-" + userId + "-" + (status ^ 1))
                            } else {
                                $btn.removeClass("btn-danger");
                                $btn.addClass("btn-success");
                                $btn.html("已激活");
                                $btn.html('<i class="fa fa-check"></i>&nbsp;已激活');
                                $btn.attr("id", "edit-user-status-" + userId + "-" + (status ^ 1))
                            }
                        } else {
                            swal("账号状态修改失败！", "", "error");
                        }
                    },
                    error: errorAlert
                });
            }
        });
    })
}

function initPageSizeSel() {
    $("#pageSize-sel").change(function () {
        pageSize = $(this).val();
        goPage(1);
    });
}

function initIdentityBtn() {
    $("button[id*='user-btn']").click(function () {
        $("button[id^='user-btn']").removeClass("btn-warning");
        $(this).addClass("btn-warning");
        identity = this.id.split("-")[2];
        goPage(1);
    });
}

function initSearchBtn() {
    $("#user-search-btn").click(function () {
        goPage(1);
    })
}