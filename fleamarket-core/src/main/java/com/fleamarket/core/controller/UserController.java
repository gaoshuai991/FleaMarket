package com.fleamarket.core.controller;

import com.fleamarket.core.model.Treasure;
import com.fleamarket.core.model.TreasurePicture;
import com.fleamarket.core.model.TreasureStar;
import com.fleamarket.core.model.User;
import com.fleamarket.core.service.*;
import com.fleamarket.core.util.Constant;
import com.fleamarket.core.util.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("user")
public class UserController {
    private final TreasureService treasureService;
    private final TreasurePictureService treasurePictureService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final UploadService uploadService;
    private final TreasureStarService treasureStarService;

    @Autowired
    public UserController(TreasureService treasureService, TreasurePictureService treasurePictureService, UserService userService, CategoryService categoryService, UploadService uploadService, TreasureStarService treasureStarService) {
        this.treasureService = treasureService;
        this.treasurePictureService = treasurePictureService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.uploadService = uploadService;
        this.treasureStarService = treasureStarService;
    }

    @GetMapping("user_center")
    public String userCenter(HttpServletRequest request) {
        request.setAttribute("treasures", treasureService.selectTreasureByUid(Utils.getUserSession(request.getSession()).getId()));
        request.setAttribute("newDegrees", Constant.NEW_DEGREE_LIST);
        request.setAttribute("categories", categoryService.selectAllChildren());
        return "user/user_center";
    }

    /**
     * 用户基本信息修改
     * @param user
     * @param request
     * @return
     */
    @PutMapping("user_center/basic")
    @ResponseBody
    public boolean updateUserBaisc(User user,HttpServletRequest request){
        String work_province = request.getParameter("work_province");
        String work_city = request.getParameter("work_city");
        if(!StringUtils.isEmpty(work_province)){
            user.setCity(StringUtils.isEmpty(work_city) ? work_province : work_province + "-" + work_city);
        }
        String school_province = request.getParameter("school_province");
        String school_name = request.getParameter("school_name");
        if(!StringUtils.isEmpty(school_province)){
            user.setSchool(StringUtils.isEmpty(school_name) ? school_province : school_province + "-" + school_name);
        }
        if(userService.updateByPrimaryKeySelective(user)) {
            request.getSession().setAttribute("user", userService.selectByPrimaryKey(user.getId()));
            return true;
        }
        return false;
    }

    /**
     * 商品修改
     * @param treasure
     * @param request
     * @return
     */
    @PutMapping("user_center/treasure")
    @ResponseBody
    public boolean updateTreasure(Treasure treasure, HttpServletRequest request){
        List<String> methods = Arrays.asList(request.getParameterValues("tradingMethod"));
        treasure.setTradingMethod((methods.contains("pickUp") ? "1" : "0") + (methods.contains("faceGay") ? "1" : "0") + (methods.contains("postMan") ? "1" : "0"));
        treasure.setTotal(treasure.getPrice() + treasure.getFare());
        return treasureService.updateByPrimaryKeySelective(treasure);
    }
    /**
     * 宝贝图片上传
     *
     * @param request
     * @return 严格返回JSON格式
     */
    @PostMapping(value = "user_center/treasure_pic")
    @ResponseBody
    public Object treasurePictureUpdate(HttpServletRequest request) throws Exception {
        //先检查图片数量是否已经上传至最大，即8张
        Integer tid = Integer.parseInt(request.getParameter("tid"));
        int existsCount = treasurePictureService.selectAllTreasurePicture(tid).size();
        if (existsCount >= 8) {
            return "{\"result\":\"false\"}";
        }
        Map<String, Object> resultMap = new HashMap<>();
        //如果符合以上条件就给予上传
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> pictures = mrequest.getFiles("pictures");
            if (existsCount + pictures.size() > 8) {
                return "{\"result\":\"false\"}";
            }
            for (MultipartFile picture : pictures) {
                if (picture != null) {
                    log.debug("图片名称====>" + picture.getName());
                    log.debug("图片类型====>" + picture.getContentType());
                    log.debug("图片大小====>" + picture.getSize());
                    String picturePath = uploadService.uploadFile(picture);
                    if (picturePath == null) {
                        log.debug("宝贝图片上传失败!");
                        return "{\"result\":\"false\"}";
                    }else{
                        TreasurePicture treasurePicture = new TreasurePicture();
                        treasurePicture.setTreasureId(tid);
                        treasurePicture.setPicture(picturePath);
                        if (!treasurePictureService.insertTreasurePicture(treasurePicture)){
                            uploadService.deleteFile(picturePath);
                            log.debug("宝贝图片上传失败!");
                            return "{\"result\":\"false\"}";
                        }
                    }
                    log.debug("宝贝图片上传完成!");
                }
            }
            //查询出所有的照片
            resultMap.put("pictures", treasurePictureService.selectAllTreasurePicture(tid));
        }
        resultMap.put("result", "true");
        return resultMap;
    }

    /**
     * 删除商品图片
     * @param tpid
     * @return
     */
    @DeleteMapping("user_center/treasure_pic/{tpid}")
    @ResponseBody
    public boolean treasurePictureDelete(@PathVariable Integer tpid){
        String picture = treasurePictureService.selectByPrimaryKey(tpid).getPicture();
        return treasurePictureService.deleteTreasurePicture(tpid) && uploadService.deleteFile(picture);
    }

    /**
     * 把某个图片设为商品主图片
     * @param tpid
     * @param tid
     * @return
     */
    @PutMapping("user_center/treasure_main_pic")
    @ResponseBody
    @Transactional
    public boolean setTreasureMainPicture(Integer tpid, Integer tid){
        String mainPic = treasureService.selectByPrimaryKey(tid).getPicture();
        String pic = treasurePictureService.selectByPrimaryKey(tpid).getPicture();
        Treasure treasure = new Treasure();
        treasure.setId(tid);
        treasure.setPicture(pic);
        TreasurePicture treasurePicture = new TreasurePicture();
        treasurePicture.setId(tpid);
        treasurePicture.setPicture(mainPic);
        return treasureService.updateByPrimaryKeySelective(treasure) && treasurePictureService.updateByPrimaryKeySelective(treasurePicture);
    }

    /**
     * 发布新商品页面跳转
     * @param request
     * @return
     */
    @GetMapping("treasure")
    public String publishTreasure(HttpServletRequest request){
        request.setAttribute("newDegrees", Constant.NEW_DEGREE_LIST);
        request.setAttribute("categories", categoryService.selectAllChildren());
        return "user/treasure_publish";
    }
    @GetMapping("success")
    public String publishSuccess(){
        return "user/publish_success";
    }

    /**
     * 发布新商品
     * @param treasure
     * @param photo
     * @param request
     * @return
     */
    @PostMapping("treasure")
    public String publishTreasure(Treasure treasure, MultipartFile photo, HttpServletRequest request){
        User userSession = Utils.getUserSession(request.getSession());
        String pickUp = request.getParameter("pickUp");
        String faceGay = request.getParameter("faceGay");
        String postMan = request.getParameter("postMan");
        String tradingMethod = (pickUp == null ? "0" : pickUp) + (faceGay == null ? "0" : faceGay) + (postMan == null ? "0" : postMan);
        treasure.setTradingMethod(tradingMethod);
        treasure.setTotal(treasure.getPrice() + treasure.getFare());
        treasure.setUid(userSession.getId());
        treasure.setLocation(userSession.getCity());
        try {
            String fileName;
            if ((fileName = uploadService.uploadFile(photo)) != null) {
                treasure.setPicture(fileName);
                if(treasureService.treasurePublish(treasure)){
                    return "user/publish_success"; // TODO
                }else{
                    uploadService.deleteFile(fileName);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return "user/treasure_publish";
    }

    /**
     * 获取单个商品信息
     * @param treasureId
     * @return
     */
    @GetMapping("treasure/{treasureId}")
    @ResponseBody
    public Map<String, Object> getTreasure(@PathVariable Integer treasureId) {
        final Treasure treasure = treasureService.selectByPrimaryKey(treasureId);
        final char[] tradingMethods = treasure.getTradingMethod().toCharArray();
        return new HashMap<String, Object>(){{
            put("treasure", treasure);
            put("pictures", treasurePictureService.selectAllTreasurePicture(treasureId));
            put("pickUp", tradingMethods[0] == '1');
            put("faceGay", tradingMethods[1] == '1');
            put("postMan", tradingMethods[2] == '1');
        }};
    }

    @PostMapping("treasure/star")
    @ResponseBody
    public boolean treasureStar(Integer tid, HttpSession session){
        TreasureStar treasureStar = new TreasureStar();
        treasureStar.setUserId(Utils.getUserSession(session).getId());
        treasureStar.setTreasureId(tid);
        return treasureStarService.insertSelective(treasureStar);
    }
    @PostMapping("treasure/unstar")
    @ResponseBody
    public boolean treasureUnstar(Integer tid, HttpSession session){
        TreasureStar treasureStar = new TreasureStar();
        treasureStar.setUserId(Utils.getUserSession(session).getId());
        treasureStar.setTreasureId(tid);
        return treasureStarService.delete(treasureStar) == 1;
    }

}
