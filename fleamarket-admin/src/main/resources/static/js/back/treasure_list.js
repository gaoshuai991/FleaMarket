var pageNum = 1;
var pageSize = 5;
var pageSizeArray = [1, 3, 5, 8, 10];
var status = 1;

$(function () {
    setPageSizeSel(pageSizeArray, pageSize);
    loadData();
    initPageSizeSel();
    initStatusBtn();
    initSearchBtn();
    $('#category-sel').change(function () {
       goPage(1);
    });
});

function loadData() {
    $.get(contextPath + "admin/treasures/",
        {
            "status" : status,
            "categoryId" : $('#category-sel').val(),
            "pageNum": pageNum,
            "pageSize": pageSize,
            "column": $("#basic-search-sel").val(),
            "keyword": $("#basic-search-val").val()
        }, function (data) {
            var $userBasicTBody = $("#user-basic-table").find(">tbody");
            $userBasicTBody.empty();
            for (var x = 0; x < data.list.length; x++) {
                var treasure = data.list[x];
                var arr = treasure.tradingMethod.split("");
                // var descritpion = treasure.description.length > 30 ? (treasure.description.substring(0, 30) + '......') : treasure.description;
                var descritpion = treasure.description;
                var tm = (arr[0] == "1" ? "自提 " : "") + (arr[1] == "1" ? "同城面交 " : "") + (arr[2] == "1" ? "邮寄 " : "");
                var tr = $('<tr>\n' +
                    '         <td><img src="' + contextPath + 'file?path=' + treasure.picture + '" class="photo"></td>\n' +
                    '         <td>' + treasure.id + '</td>\n' +
                    '         <td>' + treasure.uid + '</td>\n' +
                    '         <td>' + treasure.title + '</td>\n' +
                    '         <td>' + descritpion + '</td>\n' +
                    '         <td>' + treasure.category + '</td>\n' +
                    '         <td>￥' + treasure.price + '</td>\n' +
                    '         <td>￥' + treasure.fare + '</td>\n' +
                    '         <td>' + tm + '</td>\n' +
                    '         <td>' + treasure.newDegree + '</td>\n' +
                    '         <td>' + treasure.createTime + '</td>\n' +
                    '    </tr>');
                if (treasure.status === 0) {
                    tr.append($('<td><button id="edit-user-status-' + treasure.id + '-0" class="btn btn-sm btn-danger"><i class="fa fa-times"></i>&nbsp;下架</button></td>'));
                } else if (treasure.status === 1) {
                    tr.append($('<td><button id="edit-user-status-' + treasure.id + '-1" class="btn btn-sm btn-success"><i class="fa fa-check"></i>&nbsp;在售</button></td>'));
                }else if (treasure.status === 2) {
                    tr.append($('<td><button id="edit-user-status-' + treasure.id + '-1" class="btn btn-sm btn-success"><i class="fa fa-check"></i>&nbsp;已售出</button></td>'));
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

function initStatusBtn() {
    $(".status-btn").click(function () {
        $(".status-btn").removeClass("btn-warning");
        $(this).addClass("btn-warning");
        status = $(this).val();
        goPage(1);
    });
}

function initSearchBtn() {
    $("#user-search-btn").click(function () {
        goPage(1);
    })
}