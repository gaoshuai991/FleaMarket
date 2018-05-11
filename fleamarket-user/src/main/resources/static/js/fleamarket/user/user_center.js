$(function () {
    initUserBasic();
});

function initUserBasic() {
    if(city !== ''){
        var cityArr = city.split("-");
        if(cityArr.length == 1){
            initWorkplace("work_province", "work_city", cityArr[0], "-1");
        }else if(cityArr.length == 2){
            initWorkplace("work_province", "work_city", cityArr[0], cityArr[1]);
        }
    }
    if(school !== ''){
        var schoolArr = school.split("-");
        if(schoolArr.length == 1){
            initCampus("school-province", "school-name", schoolArr[0], "-1");
        }else if (schoolArr.length ==2){
            initCampus("school-province", "school-name", schoolArr[0], schoolArr[1]);
        }
    }
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
function deletePhoto(delBtnInstance) {
    //找到他父节点div
    if(confirm("您确定删除此照片吗？")) {
        $.ajax({
            url: contextPath + "usercenter/photo/" + $(delBtnInstance).attr("id").split("-")[2],
            type: "DELETE",
            dataType: "TEXT",
            success: function (data) {
                if (data == "true") {
                    var $delId = $(delBtnInstance).closest("div");
                    //隐藏不可上传按钮
                    $("#max-upload").hide();
                    //显示可上传按钮
                    $("#min-upload").show();
                    $delId.remove();
                    swal("温馨提示", "删除成功！", "success");
                } else {
                    swal("温馨提示", "删除失败！", "error");
                }
            }
        });
    }

}

//照片上传
function initPhotoUpload() {
    //照片批量上传
    $("#file-1").fileinput({
        language: 'zh',
        theme: 'fa',
        uploadUrl: contextPath + "usercenter/upload", // you must set a valid URL here else you will get an error
        allowedFileExtensions: ['jpg', 'png', 'gif'],
        overwriteInitial: false,
        maxFileSize: 1024*5,
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
    //上传成功回调,没上传成功一次就回调一次 filepreupload fileuploaded
    /* $("#file-1").on("filepreupload", function (event, data, previewId, index) {
         $("#myModal").modal("hide");
         if(data.response.result=="true"){
             alert(JSON.stringify(data.response));
             alert(data.response.result);
             swal("温馨提示","上传成功！","success");
             return true;
         }else {
             alert(JSON.stringify(data.response));
             alert(data.response.result);
             swal("温馨提示","不能超过八张","error");
             return false;
         }

     });
 */
    //同步上传成功回调
    $('#file-1').on('filebatchuploadsuccess', function(event, data, previewId, index) {
        $("#myModal").modal("hide");
        if(data.response.result=="true"){
            var photos = data.response.photos;
            // alert(JSON.stringify(data.response));
            // alert(photos);
            for(var i=0;i<photos.length;i++){
                var photoDiv="<div class='photo-single' id='photo-single-"+photos[i].id+"'>" +
                    "             <a href='"+contextPath+"file?path="+photos[i].photo+"'" +
                    "                data-toggle='lightbox' data-gallery='sigma-gallery'" +
                    "                data-title='Image Title 01'>" +
                    "                 <img src='"+contextPath+"file?path="+photos[i].photo+"'" +
                    "                      alt='第1张'" +
                    "                      class='img-fluid sigmapad'>" +
                    "                 <a id='photo-del-"+photos[i].id+"' onclick='deletePhoto(this)' class='photo-del'><i" +
                    "                         class='fa fa-times'></i></a>" +
                    "             </a>" +
                    "             <div class='submit inline-block'>" +
                    "                 <button id='photo-btn-"+photos[i].id+"' class='hvr-wobble-vertical set-photo-btn'>设为头像</button>" +
                    "             </div>" +
                    "         </div>";

                $("div[class='photo-single']:last").after($(photoDiv));
                //成功之后给所用div再次添加响应事件
                initSetPhotoBtn();
            }
            //这里是判断如果图片达到8张了，就把图片上传按钮禁用
            if($("div[id^='photo-single']").size()<8){
                //隐藏不可上传按钮
                $("#max-upload").hide();
                //显示可上传按钮
                $("#min-upload").show();
            }else {
                //显示不可上传按钮
                $("#max-upload").show();
                //隐藏可上传按钮
                $("#min-upload").hide();
            }
            swal("温馨提示","上传成功！","success");
        }else {
            // alert(JSON.stringify(data.response));
            // alert(data.response.result);
            swal("温馨提示","不能超过八张","error");
        }
    });

    //头像上传
    $("#img").fileinput({
        language: 'zh',
        theme: 'fa',
        uploadUrl: contextPath + "usercenter/photo", // you must set a valid URL here else you will get an error
        allowedFileExtensions: ['jpg', 'png', 'gif'],
        overwriteInitial: false,
        maxFileSize: 1024*5,
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
        if(data.response.result){
            var photo = data.response.photo;
            $("#head-sculpture").attr("src",contextPath+"file?path="+photo.photo);
            swal("温馨提示","上传成功！","success");
            var photoDiv="<div class='photo-single' id='photo-single-"+photo.id+"'>" +
                "             <a href='"+contextPath+"file?path="+photo.photo+"'" +
                "                data-toggle='lightbox' data-gallery='sigma-gallery'" +
                "                data-title='Image Title 01'>" +
                "                 <img src='"+contextPath+"file?path="+photo.photo+"'" +
                "                      alt='第1张'" +
                "                      class='img-fluid sigmapad'>" +
                "             </a>" +
                "             <div class='submit inline-block'>" +
                "                 <button id='photo-btn-"+photo.id+"' class='hvr-wobble-vertical set-photo-btn'>当前头像</button>" +
                "             </div>" +
                "         </div>";
            $("div[class='photo-single']:first").remove();
            $("#photoContent").prepend($(photoDiv));
            initSetPhotoBtn();
        }else{
            swal("温馨提示","上传失败！","error");
        }
    });
}