package cn.yumutech.netUtil.inter;

import cn.yumutech.bean.AcceptTask;
import cn.yumutech.bean.AddErrorPinglun;
import cn.yumutech.bean.AddPingLun;
import cn.yumutech.bean.AssignTask;
import cn.yumutech.bean.BumenStatusItem;
import cn.yumutech.bean.BumenStatusList;
import cn.yumutech.bean.ChaXunQunMenmber;
import cn.yumutech.bean.CompleteBean;
import cn.yumutech.bean.CreateQunZu;
import cn.yumutech.bean.DepartListNew;
import cn.yumutech.bean.DeviceTokenBean;
import cn.yumutech.bean.ExchangeCommenList;
import cn.yumutech.bean.ExchangeSearch;
import cn.yumutech.bean.FinishTask;
import cn.yumutech.bean.GetTaShanPingLunLieBIao;
import cn.yumutech.bean.GroupDetais;
import cn.yumutech.bean.HuDongItem;
import cn.yumutech.bean.HuDongJIaoLiu;
import cn.yumutech.bean.JieSanQun;
import cn.yumutech.bean.JoinQun;
import cn.yumutech.bean.LeaderActivitsDetails;
import cn.yumutech.bean.LeaderActivitys;
import cn.yumutech.bean.ModuleClassifyList;
import cn.yumutech.bean.MovieRecommend;
import cn.yumutech.bean.PolicyFileSearch;
import cn.yumutech.bean.ProjectManger;
import cn.yumutech.bean.ProjectWorkSearch;
import cn.yumutech.bean.ProjectialsXiangqing;
import cn.yumutech.bean.PublishTask;
import cn.yumutech.bean.RefreshBean;
import cn.yumutech.bean.ShowMyPublishedTask;
import cn.yumutech.bean.ShowMyTask;
import cn.yumutech.bean.ShowTaskDetail;
import cn.yumutech.bean.ShuaXinQunZhu;
import cn.yumutech.bean.TaskNotifiList;
import cn.yumutech.bean.TaskNotificationSearch;
import cn.yumutech.bean.TuiChuQun;
import cn.yumutech.bean.Update;
import cn.yumutech.bean.UpdateUserPhoto;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.bean.UserInfoDetail;
import cn.yumutech.bean.UserLogin;
import cn.yumutech.bean.UserToken;
import cn.yumutech.bean.UserXiangGuanQun;
import cn.yumutech.bean.WorkDetails;
import cn.yumutech.bean.WorkListManger;
import cn.yumutech.bean.WorkStatusSearch;
import cn.yumutech.bean.XianShiRenWuXiangQing;
import cn.yumutech.bean.XianStatusItem;
import cn.yumutech.bean.XianStatusList;
import cn.yumutech.bean.YanZhenMessageBean;
import cn.yumutech.bean.YouQIngLianJie;
import cn.yumutech.bean.ZhengCeFile;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 霍长江 on 2016/8/5.
 */
public interface ApiServer {
    //登录接口
    @POST("UserLogin")
    Observable<UserLogin> getUserLogin(@Query("req") String messge);
    //登录接口
    @POST("UserToken")
    Observable<UserToken> getUserToken(@Query("req") String messge);
    //他山之石评论列表
    @POST("ExchangeCommentList")
    Observable<ExchangeCommenList> getExchangeCommenList(@Query("req") String list);
    //督察督办之发布任务
    @POST("PublishTask")
    Observable<PublishTask> getPublishTask(@Query("req") String list);
    //督察督办之显示我发布任务
    @POST("ShowMyPublishedTask")
    Observable<ShowMyPublishedTask> getShowMyPublishedTask(@Query("req") String list);
    //督察督办之显示任务详情
    @POST("ShowTaskDetail")
    Observable<ShowTaskDetail> getShowTaskDetail(@Query("req") String list);
    //督察督办之显示我的任务
    @POST("ShowMyTask")
    Observable<ShowMyTask> getShowMyTask(@Query("req") String list);
    //督察督办之显示我发布的任务
    @POST("ShowMyPublishedTask")
    Observable<ShowMyPublishedTask> getXianShiFaBu(@Query("req") String item);
    //督察督办之显示接受任务
    @POST("AcceptTask")
    Observable<AcceptTask> getAcceptTask(@Query("req") String item);
    //督察督办之完成任务
    @POST("FinishTask")
    Observable<FinishTask> getFinishTask(@Query("req") String item);
    //督察督办之指派任务
    @POST("AssignTask")
    Observable<AssignTask> getAssignTask(@Query("req") String item);
    //1.15.1获取模块分类
    @POST("ModuleClassifyList")
    Observable<ModuleClassifyList> getModuleClassifyList(@Query("req") String item);
    //更改用户头像
    @POST("UpdateUserPhoto")
    Observable<UpdateUserPhoto> getUpdateUserPhoto(@Query("req") String item);


    //影视推荐
    @GET("rest/VideoSourceService/getVideoIndex?u_=1&v_=1&t_=1&p_=2348")
    Observable<MovieRecommend> getMovieRecmmend();
    //获取首页
    @GET("LeaderActivityList")
    Observable<LeaderActivitys> getLeaderActiviys(@Query("req") String list);
    //获取领导活动
    //获取首页
    @GET("HomeSliderList")
    Observable<LeaderActivitys> getHomeLunBo(@Query("req") String list);

    //获取领导活动详情页面
    @POST("LeaderActivityItem")
    Observable<LeaderActivitsDetails> getLeaderActivitsDetails(@Query("req") String item);
    //获取政策文件管理
    @POST("PolicyFileList")
    Observable<ZhengCeFile> getPolicy(@Query("req") String policy);
    //获取政策文件详情
    @POST("PolicyFileItem")
    Observable<LeaderActivitsDetails> getPolicyDetais(@Query("req") String policy);

    //获取工作状态
    @POST("WorkStatusList")
    Observable<WorkListManger> getWorkStatus(@Query("req") String policy);
//获取工作动态详情

@POST("WorkStatusItem")
Observable<WorkDetails> getWorkDetais(@Query("req") String policy);
    //获取项目工作管理
    @POST("ProjectWorkList")
    Observable<ProjectManger>  getProject(@Query("req") String prject);
    //获取项目管理详情
    @POST("ProjectWorkItem")
    Observable<ProjectialsXiangqing> getProjectDetais(@Query("req") String projectdetais);
//获取友情链接
    @POST("LinkList")
    Observable<YouQIngLianJie> getFrindeUrl(@Query("req") String projectdetais);
    //验证码
    @POST("UserValidCode")
    Observable<YanZhenMessageBean> getMessageVail(@Query("req") String messge);
    //获取互动交流接口
    @POST("ExchangeList")
    Observable<HuDongJIaoLiu> getHuDongJiaoLiu(@Query(("req")) String jiaoliu);
    //获取他山之时详情数据
    @POST("ExchangeItem")
    Observable<HuDongItem> getHuDongItem(@Query("req") String item);
    //刷新群主信息
    @POST("GroupRefresh")
    Observable<ShuaXinQunZhu> getShuaXinQunZhu(@Query("req") String item);
    //退出群
    @POST("GroupQuit")
    Observable<TuiChuQun> getTuiChuQun(@Query("req") String item);
    //获取区县动态数据
    @POST("XianStatusList")
    Observable<XianStatusList> getXianStatusList(@Query("req") String item);
    //获取区县动态数据详情
    @POST("XianStatusItem")
    Observable<XianStatusItem> getXianStatusItem(@Query("req") String item);
    //获取部门动态数据
    @POST("BumenStatusList")
    Observable<BumenStatusList> getBumenStatusList(@Query("req") String item);
    //获取部门动态数据详情
    @POST("BumenStatusItem")
    Observable<BumenStatusItem> getBumenStatusItem(@Query("req") String item);
    //查看群成员
//    @POST("GroupQueryUser")



    //查看群成员
//    @POST("GroupQueryUser")

//获取他山之时评论列表数据
    @POST("ExchangeCommentList")
    Observable<GetTaShanPingLunLieBIao> getPingLunLieBiao(@Query("req") String item);
    //添加他山之石评论
    @POST("ExchangeCommentAdd")
    Observable<AddErrorPinglun> getAddPingLun(@Query("req") String item);
    //添加他山之石评论
    @POST("ExchangeCommentAdd")
    Observable<AddPingLun> getNormalAddPingLun(@Query("req") String item);
    //添加创建群组
    @POST("GroupCreate")
    Observable<CreateQunZu> getCreateQunZhu(@Query("req") String item);
    //解散群
    @POST("GroupDismiss")
    Observable<JieSanQun> getJieSanQun(@Query("req") String item);
    //加入群主
    @POST("GroupJoin")
    Observable<JoinQun> getJoinQun(@Query("req") String item);
    //查询群成员
    @POST("QueryUserList")
    Observable<ChaXunQunMenmber> getChaXunMember(@Query("req") String item);
//提交完成的任務
    @POST("FinishTask")
    Observable<CompleteBean> getComplete(@Query("req") String item);
    //通知通报
    @POST("TaskNotificationList")
    Observable<TaskNotifiList> getTaskNotifiList(@Query("req") String item);
    //政策文件搜索
    @POST("PolicyFileSearch")
    Observable<PolicyFileSearch> getPolicyFileSearch(@Query("req") String item);
    //动态消息搜索
    @POST("WorkStatusSearch")
    Observable<WorkStatusSearch> getWorkStatusSearch(@Query("req") String item);
    //重点项目搜索
    @POST("ProjectWorkSearch")
    Observable<ProjectWorkSearch> getProjectWorkSearch(@Query("req") String item);
    //他山之石搜索
    @POST("ExchangeSearch")
    Observable<ExchangeSearch> getExchangeSearch(@Query("req") String item);
    //通知通报查询接口
    @POST("TaskNotificationSearch")
    Observable<TaskNotificationSearch> getTaskNotificationSearch(@Query("req") String item);


    //显示任务详情
    @POST("ShowTaskDetail")
    Observable<XianShiRenWuXiangQing> getRenWuDetail(@Query("req") String item);
    //获取部门列表数据
    @POST("DeptList")
    Observable<DepartListNew> getBumenList(@Query("req") String item);
    //获取用户列表
    //查询用户相关的群
    @POST("QueryGroupList")
    Observable<UserXiangGuanQun> getUserXiangGuanQun(@Query("req") String item);
    //获取用户相关列表
    @POST("UserList")
    Observable<UserAboutPerson> getUserAboutPerson(@Query("req") String item);
    //获取用户详情
    @POST("UserItem")
    Observable<UserInfoDetail> getUserDetais(@Query("req") String item);
    //获取组详情数据
    @POST("LeaderActivityItem")
    Observable<GroupDetais> getGroupDetais(@Query("req") String item);
    //刷新群主信息
    @POST("GroupRefresh")
    Observable<RefreshBean> getRefersh(@Query("req") String item);
    //上傳設備的token
    @POST("DeviceToken")
    Observable<DeviceTokenBean> getDevieTokenBean(@Query("req") String item);
    //檢查設備信息
    @POST("CheckLatestVersion")
    Observable<Update> getSheBeiXinXi(@Query("req") String item);
}
