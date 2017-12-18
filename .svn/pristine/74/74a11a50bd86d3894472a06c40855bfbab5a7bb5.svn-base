package com.eastsoft.esgjyj.controller;

import java.util.List;

import com.eastsoft.esgjyj.domain.Office;
import com.eastsoft.esgjyj.service.impl.OfficeServiceImpl;
import com.eastsoft.esgjyj.util.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eastsoft.esgjyj.context.ScopeUtil;
import com.eastsoft.esgjyj.domain.User;
import com.eastsoft.esgjyj.form.TreeNode;
import com.eastsoft.esgjyj.service.impl.UserServiceImpl;

/**
 * 用户控制器。
 *
 * @author Ben
 * @version 1.0.0
 * @since 1.0.0
 */
@Controller
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private OfficeServiceImpl officeService;

    /**
     * 获取当前用户模块树。
     *
     * @param request HTTP 请求对象。
     * @return 模块树节点列表。
     */
    @GetMapping("/user/program/tree")
    @ResponseBody
    public List<TreeNode> getProgramTreeNodes() {
        User sessionUser = ShiroUtils.getUser();
        List<TreeNode> treeNodeList = this.userService.getProgramTreeNodes(sessionUser.getUserid());
        //
//		List<TreeNode> list = new ArrayList<>();
//		TreeNode root = new TreeNode();
//		list.add(root);
//		TreeNode child1 = new TreeNode();
//		TreeNode child2 = new TreeNode();
//		root.setText("测试一");
//		List<TreeNode> children = new ArrayList<>();
//		root.setChildren(children);
//		children.add(child1);
//		children.add(child2);
//		child1.setText("测试一一");
//		child2.setText("测试一二");
//		Map<String,Object> att = new HashMap<>();
//		att.put("icon","fa fa-user");
//		child1.setAttributes(att);
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		return list;
        return treeNodeList;
    }

    @GetMapping("/user/tree")
    @ResponseBody
    public TreeNode getUserTree() {
        User sessionUser = (User) ScopeUtil.getRequest().getSession().getAttribute("user");
        List<TreeNode> list = this.officeService.getOfficeUserTreeNodes(sessionUser.getCourtNo());
        TreeNode root = new TreeNode();
        root.setText("根节点");
        root.setId("-1");
        root.setChildren(list);
        return root;
    }

    @GetMapping("/getOfficeByUserId")
    @ResponseBody
    public Office getOfficeByUserid(String userId){
        return userService.getOfficeByuserId(userId);
    }
}
