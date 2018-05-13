$(function () {
    setPanel();
    initUserBasic();
    initTreasure();
    initPictureUpload();
    initPhotoWall();
});
function setPanel() {
    var $panels = $("div[id$='Panel']");
    $panels.removeClass("active");
    if(type == null || type == ""){
        $("#basicPanel").addClass("active");
    }else{
        $("#"+type+"Panel").addClass("active");
    }
}

function initTreasure() {
    var $treasureSel = $('#treasure-select');
    $treasureSel.change(function () {
        var tid = $(this).val();
        if (tid == '0') {
            $('#form-treasure')[0].reset();
            $('input[type="checkbox"]').attr('checked', false);
            $('textarea').text('');
            return;
        }
        $('#tid').val(tid);
        $.get(contextPath + "user/treasure/" + tid, function (data) {
            var treasure = data['treasure'];
            $('#title').val(treasure.title);
            $('#category').val(treasure.category);
            $('#newDegree').val(treasure.newDegree);
            $('#views').text(treasure.viewCount);
            $('#stars').text(treasure.starCount);
            $('#create-time').text(treasure.createTime);
            $('#comments').text(treasure.commentCount);
            $('#price').val(treasure.price);
            $('#fare').val(treasure.fare == 'null' ? '' : treasure.fare);
            $('#pickUp').attr('checked', data['pickUp']);
            $('#faceGay').attr('checked', data['faceGay']);
            $('#postMan').attr('checked', data['postMan']);
            $('#description').text(treasure.description);
            $('#postMan').trigger("change");
            $('#main-pic').attr('src', contextPath + 'file?path=' + treasure.picture);
            $('#main-pic').closest('a').attr('href', contextPath + 'file?path=' + treasure.picture);
            initPictures(data['pictures']);
        }, "json");
    });

    if(treaid != null && treaid != ''){
        $treasureSel.val(treaid);
    }
    $treasureSel.trigger("change");

    $('#postMan').change(function () {
        $('#trading-method-div').css('display', this.checked ? 'block' : 'none');
    });

    $("#form-treasure").validator({
        rules: {
            // 使用正则表达式定义规则
            number: [/^1[3-9]\d{9}$/, "请填写有效的手机号"],
            nickname: [/^([\u4E00-\u9FA5]|\w){2,8}$/, "昵称应为2-8位字符"]
        },
        fields: {
            'title': 'required;length(~25)',
            'category': 'required',
            'newDegree': 'required',
            'price': 'required;digits',
            'fare': 'required(#isCompany:checked);digits',
            'tradingMethod': 'checked(1~)',
            'description': 'required;length(~120)'
        },
        theme: 'bootstrap',
        timely: 2,
        stopOnError: true,
        valid: function (form) {
            updateData(form, "treasure");
        }
    });
}

//修改或添加用户通用方法
function updateData(form, exturl) {
    $.ajax({
        url: contextPath + "user/user_center/" + exturl,
        data: $(form).serialize(),
        type: "PUT",
        dataType: "TEXT",
        success: function (data) {
            if (data == "true") {
                swal("温馨提示", "修改成功", "success");
            } else {
                swal("温馨提示", "修改失败", "error");
            }
        }
    });
}

function initUserBasic() {
    if (city !== '') {
        var cityArr = city.split("-");
        if (cityArr.length == 1) {
            initWorkplace("work_province", "work_city", cityArr[0], "");
        } else if (cityArr.length == 2) {
            initWorkplace("work_province", "work_city", cityArr[0], cityArr[1]);
        }
    }
    if (school !== '') {
        var schoolArr = school.split("-");
        if (schoolArr.length == 1) {
            initCampus("school_province", "school_name", schoolArr[0], "");
        } else if (schoolArr.length == 2) {
            initCampus("school_province", "school_name", schoolArr[0], schoolArr[1]);
        }
    }
    $("#form-user-basic").validator({
        rules: {
            // 使用正则表达式定义规则
            tel: [/^1[3-9]\d{9}$/, "请填写有效的手机号"],
            nickname: [/^([\u4E00-\u9FA5]|\w){2,8}$/, "昵称应为2-8位字符"]
        },
        fields: {
            'nickname': 'required;nickname',
            'tel': 'required;tel',
            'email': 'required;email'
        },
        theme: 'bootstrap',
        timely: 2,
        stopOnError: true,
        valid: function (form) {
            updateData(form, "basic");
        }
    });
}

//初始化地区下拉列表
function initWorkplace(provinceId, cityId, provinceValue, cityValue) {
    var $provinceSel = $("#" + provinceId);
    var $citySel = $("#" + cityId);
    $provinceSel.find("option:gt('0')").remove();
    $.ajaxSettings.async = false;
    $.getJSON(contextPath + "json/cities.json", function (data) {
        for (var x = 0; x < data.length; x++) {
            if (data[x].name == provinceValue)
                $provinceSel.append($("<option value='" + data[x].name + "' selected>" + data[x].name + "</option>"));
            else
                $provinceSel.append($("<option value='" + data[x].name + "'>" + data[x].name + "</option>"));
        }
    });
    if (provinceValue != undefined && provinceValue != "-1") {
        $.getJSON(contextPath + "json/cities.json", function (data) {
            for (var x = 0; x < data.length; x++) {
                if (data[x].name == $provinceSel.val()) {
                    for (var y = 1; y < data[x].sub.length; y++) {
                        if (data[x].sub[y].name == cityValue) {
                            $citySel.append($("<option value='" + data[x].sub[y].name + "' selected>" + data[x].sub[y].name + "</option>"));
                        } else {
                            $citySel.append($("<option value='" + data[x].sub[y].name + "'>" + data[x].sub[y].name + "</option>"));
                        }
                    }
                    break;
                }
            }
        });
    }
    $provinceSel.change(function () {
        var preCityValue = $citySel.val();
        $citySel.find(":gt('0')").remove();
        $.ajaxSettings.async = false;
        $.getJSON(contextPath + "json/cities.json", function (data) {
            for (var x = 0; x < data.length; x++) {
                if (data[x].name == $("#" + provinceId).val()) {
                    for (var y = 1; y < data[x].sub.length; y++) {
                        if (data[x].sub[y].name == preCityValue) {
                            $citySel.append($("<option value='" + data[x].sub[y].name + "' selected>" + data[x].sub[y].name + "</option>"));
                        } else {
                            $citySel.append($("<option value='" + data[x].sub[y].name + "'>" + data[x].sub[y].name + "</option>"));
                        }
                    }
                }
            }
        });
        $.ajaxSettings.async = true;
    });
    $.ajaxSettings.async = true;
}

// 获取学校信息并回填用户数据                         山东         青岛科技大学
function initCampus(provinceSelId, campusSelId, province_value, campus_value) {
    var $provinceSel = $("#" + provinceSelId);
    var $campusSel = $("#" + campusSelId);
    $provinceSel.find("option:gt(0)").remove();
    $campusSel.find("option:gt(0)").remove();
    var tempProvice = undefined;
    $.getJSON(contextPath + "json/campus.json", function (data) {
        for (var x = 0; x < data.length; x++) {
            for (var province in data[x]) {
                $provinceSel.append($('<option value="' + province + '" ' + (province === province_value ? 'selected' : '') + '>' + province + '</option>'));
                if (province == province_value) {
                    for (var y = 0; y < data[x][province].length; y++) {
                        $campusSel.append($('<option value="' + data[x][province][y] + '" ' + (data[x][province][y] === campus_value ? 'selected' : '') + '>' + data[x][province][y] + '</option>'));
                    }
                }
            }
        }
    });

    $provinceSel.change(function () {
        $.getJSON(contextPath + "json/campus.json", function (data) {
            var provinceSelVal = $provinceSel.val();
            $campusSel.find("option:gt(0)").remove();
            for (var x = 0; x < data.length; x++) {
                for (var province in data[x]) {
                    if (province == provinceSelVal) {
                        for (var y = 0; y < data[x][province].length; y++) {
                            $campusSel.append($('<option value="' + data[x][province][y] + '" ' + (data[x][province][y] === campus_value ? 'selected' : '') + '>' + data[x][province][y] + '</option>'));
                        }
                    }
                }
            }
        });
    });

}

//单个删除相册中的图片
function deletePhoto(treasurePicId, picDiv) {
    //找到他父节点div
    if (confirm("您确定删除此照片吗？")) {
        $.ajax({
            url: contextPath + "user/user_center/treasure_pic/" + treasurePicId,
            type: "DELETE",
            dataType: "TEXT",
            success: function (data) {
                if (data == "true") {
                    //隐藏不可上传按钮
                    $("#max-upload").hide();
                    //显示可上传按钮
                    $("#min-upload").show();
                    picDiv.remove();
                    swal("温馨提示", "删除成功！", "success");
                } else {
                    swal("温馨提示", "删除失败！", "error");
                }
            }
        });
    }
}

function initPicDiv() {
    $(".pics").each(function () {
        var $btn = $(this).find(" button");
        var $del = $(this).find(' a[id^="picture-del-"]');
        var treasurePicId = $del.attr('id').split('-')[2];
        $del.click(function () {
            deletePhoto(treasurePicId, $(this).closest('.pics'));
        });
        $btn.click(function () {
            setMainPic(treasurePicId);
        });
    })
}
function setMainPic(tpid) {
    $.ajax({
        url: contextPath + "user/user_center/treasure_main_pic/",
        data: {
            tpid: tpid,
            tid: $('#treasure-select').val()
        },
        type: "PUT",
        dataType: "TEXT",
        success: function (data) {
            if (data == "true") {
                swal({
                    title: "提示",
                    text: "设置成功！",
                    icon: "success",
                    buttons: ["关闭","确定"]
                }).then(result => location.replace(contextPath+"user/user_center?type=treasure&tid=" + $('#treasure-select').val()));
            } else {
                swal("温馨提示", "设置失败！", "error");
            }

        }
    })
}

//照片上传
function initPictureUpload() {
    //照片批量上传
    $("#treasure-pics").fileinput({
        language: 'zh',
        theme: 'fa',
        uploadUrl: contextPath + "user/user_center/treasure_pic?tid=" + $('#treasure-select').val(), // you must set a valid URL here else you will get an error
        allowedFileExtensions: ['jpg', 'png', 'gif'],
        overwriteInitial: false,
        maxFileSize: 1024 * 5,
        //maxFilesNum: 3,
        minFileCount: 1,
        maxFileCount: 8,
        enctype: 'multipart/form-data',
        allowedFileTypes: ['image'],
        uploadAsync: false, //同步上传
        slugCallback: function (filename) {
            return filename.replace('(', '_').replace(']', '_').replace("-", "_");
        }
    });
    //同步上传成功回调
    $('#treasure-pics').on('filebatchuploadsuccess', function (event, data, previewId, index) {
        $("#myModal").modal("hide");
        if (data.response.result == "true") {
            var pictures = data.response.pictures;
            // alert(JSON.stringify(data.response));
            // alert(pictures);
            initPictures(pictures);
            swal("温馨提示", "上传成功！", "success");
        } else {
            // alert(JSON.stringify(data.response));
            // alert(data.response.result);
            swal("温馨提示", "上传失败！", "error");
        }
    });
}
function initPictures(pictures) {
    for (var i = 0; i < pictures.length; i++) {
        var picDiv = '<div class="photo-single pics col-md-4">\n' +
            '             <a href="'+contextPath+'file?path='+pictures[i].picture+'"\n' +
            '                 data-toggle="lightbox" data-gallery="sigma-gallery">\n' +
            '                 <img src="'+contextPath+'file?path='+pictures[i].picture+'" class="img-fluid sigmapad">\n' +
            '             <a id="picture-del-'+pictures[i].id+'" class="photo-del"><i\n' +
            '                  class="fa fa-times"></i></a>\n' +
            '             </a>\n' +
            '             <div class="submit inline-block">\n' +
            '                 <button id="set-main-btn-'+pictures[i].id+'" class="hvr-wobble-vertical set-photo-btn">设为主图\n' +
            '                 </button>\n' +
            '             </div>\n' +
            '         </div>';

        $("#photoContent").append($(picDiv));
        //成功之后给所用div再次添加响应事件
        initPicDiv();
    }
    //这里是判断如果图片达到8张了，就把图片上传按钮禁用
    if ($(".photo-single").size() < 9) {
        //隐藏不可上传按钮
        $("#max-upload").hide();
        //显示可上传按钮
        $("#min-upload").show();
    } else {
        //显示不可上传按钮
        $("#max-upload").show();
        //隐藏可上传按钮
        $("#min-upload").hide();
    }
}
function userPhotoUpload() {
    //头像上传
    $("#img").fileinput({
        language: 'zh',
        theme: 'fa',
        uploadUrl: contextPath + "usercenter/photo", // you must set a valid URL here else you will get an error
        allowedFileExtensions: ['jpg', 'png', 'gif'],
        overwriteInitial: false,
        maxFileSize: 1024 * 5,
        maxFilesNum: 1,
        minFileCount: 1,
        maxFileCount: 1,
        enctype: 'multipart/form-data',
        allowedFileTypes: ['image'],
        //uploadAsync: false, //同步上传
        slugCallback: function (filename) {
            return filename.replace('(', '_').replace(']', '_').replace("-", "_");
        }
    });
    //头像异步上传成功回调
    $("#img").on("fileuploaded", function (event, data, previewId, index) {
        $("#myImg").modal("hide");
        if (data.response.result) {
            var photo = data.response.photo;
            $("#head-sculpture").attr("src", contextPath + "file?path=" + photo.photo);
            swal("温馨提示", "上传成功！", "success");
            var photoDiv = "<div class='photo-single' id='photo-single-" + photo.id + "'>" +
                "             <a href='" + contextPath + "file?path=" + photo.photo + "'" +
                "                data-toggle='lightbox' data-gallery='sigma-gallery'" +
                "                data-title='Image Title 01'>" +
                "                 <img src='" + contextPath + "file?path=" + photo.photo + "'" +
                "                      alt='第1张'" +
                "                      class='img-fluid sigmapad'>" +
                "             </a>" +
                "             <div class='submit inline-block'>" +
                "                 <button id='photo-btn-" + photo.id + "' class='hvr-wobble-vertical set-photo-btn'>当前头像</button>" +
                "             </div>" +
                "         </div>";
            $("div[class='photo-single']:first").remove();
            $("#photoContent").prepend($(photoDiv));
            initSetPhotoBtn();
        } else {
            swal("温馨提示", "上传失败！", "error");
        }
    });
}
function initPhotoWall() {
    $(document).delegate('*[data-toggle="lightbox"]', 'click', function (event) {
        event.preventDefault();
        return $(this).ekkoLightbox({
            always_show_close: true
        });
    });
}