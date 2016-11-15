package cn.yumutech.netUtil.inter;

import cn.yumutech.bean.AddPingLun;
import cn.yumutech.bean.ChaXunQunMenmber;
import cn.yumutech.bean.CreateQunZu;
import cn.yumutech.bean.ExchangeCommenList;
import cn.yumutech.bean.FaBuRenWu;
import cn.yumutech.bean.GetTaShanPingLunLieBIao;
import cn.yumutech.bean.HuDongItem;
import cn.yumutech.bean.HuDongJIaoLiu;
import cn.yumutech.bean.JieSanQun;
import cn.yumutech.bean.JoinQun;
import cn.yumutech.bean.LeaderActivitsDetails;
import cn.yumutech.bean.LeaderActivitys;
import cn.yumutech.bean.MovieRecommend;
import cn.yumutech.bean.PrijectDetaisl;
import cn.yumutech.bean.ProjectManger;
import cn.yumutech.bean.ShuaXinQunZhu;
import cn.yumutech.bean.TuiChuQun;
import cn.yumutech.bean.UserLogin;
import cn.yumutech.bean.UserToken;
import cn.yumutech.bean.WorkDetails;
import cn.yumutech.bean.WorkListManger;
import cn.yumutech.bean.XianShiMyFaBuRenWU;
import cn.yumutech.bean.XianShiRenWuXiangQing;
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

    //影视推荐
    @GET("rest/VideoSourceService/getVideoIndex?u_=1&v_=1&t_=1&p_=2348")
    Observable<MovieRecommend> getMovieRecmmend();
    //获取领导活动列表
    @GET("LeaderActivityList")
    Observable<LeaderActivitys> getLeaderActiviys(@Query("req") String list);
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
    Observable<PrijectDetaisl> getProjectDetais(@Query("req") String projectdetais);
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

    //查看群成员
//    @POST("GroupQueryUser")



    //查看群成员
//    @POST("GroupQueryUser")

//获取他山之时评论列表数据
    @POST("ExchangeCommentList")
    Observable<GetTaShanPingLunLieBIao> getPingLunLieBiao(@Query("req") String item);
    //添加他山之石评论
    @POST("ExchangeCommentAdd")
    Observable<AddPingLun> getAddPingLun(@Query("req") String item);
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
    @POST("GroupQueryUser")
    Observable<ChaXunQunMenmber> getChaXunMember(@Query("req") String item);
    //发布任务
    @POST("PublishTask")
    Observable<FaBuRenWu> getFaBuRenWu();
    //显示我发布的任务
    @POST("ShowMyPublishedTask")
    Observable<XianShiMyFaBuRenWU> getXianShiFaBu(@Query("req") String item);
    //显示任务详情
    @POST("ShowTaskDetail")
    Observable<XianShiRenWuXiangQing> getRenWuDetail(@Query("req") String item);
}
