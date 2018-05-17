$(function () {
    reloadStarBtn();
    $('#star-btn').click(function () {
        if(userSession == null){
            swal({
                title: "提示",
                text: "您尚未登录，请先登录！",
                icon: "warning",
                buttons: ["关闭","确定"]
            }).then(result => {
                if(result){
                    location.replace(contextPath + 'login?to=treasure/'+tid);
                }
            });
        }
        $.post(contextPath + "user/treasure/star", {"tid": tid}, function (data) {
            if (data == 'true') {
                isStared = true;
                reloadStarBtn();
                swal("温馨提示", "收藏成功", "success");
            } else {
                swal("温馨提示", "收藏失败", "error");
            }
        }, "text");
    });
    $('#buy-btn').click(function () {
        if(userSession == null){
            swal({
                title: "提示",
                text: "您尚未登录，请先登录！",
                icon: "warning",
                buttons: ["关闭","确定"]
            }).then(result => {
                if(result){
                    location.replace(contextPath + 'login?to=treasure/'+tid);
                }
            });
        }else {
            window.location.href =contextPath+ 'shop/' + tid;
        }
    });

    $('#unstar-btn').click(function () {
        $.post(contextPath + "user/treasure/unstar", {"tid": tid}, function (data) {
            if (data == 'true') {
                isStared = false;
                reloadStarBtn();
                swal("温馨提示", "已取消收藏", "success");
            } else {
                swal("温馨提示", "取消失败", "error");
            }
        }, "text");
    });
});

function reloadStarBtn() {
    $('#star-btn').css('display', isStared == true ? 'none' : 'inline-block');
    $('#unstar-btn').css('display', isStared == null || isStared != true ? 'none' : 'inline-block');
}