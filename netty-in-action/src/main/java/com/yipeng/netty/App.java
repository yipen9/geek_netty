package com.yipeng.netty;

import com.yipeng.netty.common.MessageHeader;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String dt = "20201021";
        String sub_sql = "SELECT vender_id,store_id,sku_id,user_id,subscribe_type,subscribe_status from dmall_search.search_subscribe  where dt = '" + dt + "' UNION all " +
                "SELECT vender_id,store_id,sku_id , user_id,type as subscribe_type,1 as subscribe_status from dmall_search.search_sys_subscribe where dt = '" + dt + "' ";

        String push_sql = " select id,vender_id,store_id,sku_id,user_id,subscribe_type," +
                " push_status,create_time,push_time " +
                " from dmall_search.search_push_list " +
                " where dt = '" + dt + "' and push_status = 4";


        // 推送到达sql
        String push_reach_sql = "SELECT t1.vender_id,t1.store_id,t1.user_id,t1.tdc,t2.sku_id,t2.subscribe_type FROM dwd_data.dwd_traffic_user_events t1 join dmall_search.search_push_list t2 on " +
                "t1.vender_id = t2.vender_id and t1.store_id = t2.store_id and t1.user_id = t2.user_id and t1.dt = t2.dt " +
                "WHERE t1.dt = '" + dt + "' and t2.dt= '" + dt + "' AND t1.event_code = 'push_reach' AND t1.tdc LIKE '51.30.0.13744-15408%' and t2.push_status = 4";

        // 推送点击sql
        String push_click_sql = "SELECT t1.vender_id,t1.store_id,t1.user_id,t1.tdc,t2.sku_id,t2.subscribe_type FROM dwd_data.dwd_traffic_user_events t1 join dmall_search.search_push_list t2 on " +
                "t1.vender_id = t2.vender_id and t1.store_id = t2.store_id  and t1.user_id = t2.user_id and t1.dt = t2.dt " +
                "WHERE t1.dt = '" + dt + "' and t2.dt= '" + dt + "' AND t1.event_code = 'push_click' AND t1.tdc LIKE '51.30.0.13744-15408%' and t2.push_status = 4";


        // 推送点击加购sql
        String add_cart_sql = " select cast(aa.vender_id AS STRING) as vender_id,cast(aa.store_id AS STRING) as store_id,count(distinct aa.user_id) as add_cart_users ,aa.subscribe_type from " +
                " (select tt.vender_id as vender_id,tt.store_id as store_id,tt.user_id as user_id,tt1.sku_id as sku_id, tt1.subscribe_type, " +
                " concat(tt.key,'-',tt1.sku_id) as key " +
                " from " +
                " (SELECT vender_id,store_id,user_id,concat(vender_id,'-',store_id,'-',user_id) as key " +
                " FROM dwd_data.dwd_traffic_user_events" +
                " WHERE dt = '" + dt + "' " +
                "     AND event_code = 'push_click' " +
                "     AND tdc LIKE '51.30.0.13744-15408%') tt " +
                " join " +
                " (select vender_id,store_id,sku_id,user_id,subscribe_type,concat(vender_id,'-',store_id,'-',user_id) as key " +
                " from dmall_search.search_push_list " +
                " where dt = '" + dt + "' and push_status = 4) tt1 " +
                " on tt.key = tt1.key ) aa " +
                " join " +
                " (SELECT vender_id,store_id,user_id,sku_id, " +
                " concat(vender_id,'-',store_id,'-',user_id,'-',sku_id) as key " +
                " FROM dmall_search.mid_add_cart " +
                " WHERE dt = '" + dt + "' " +
                "     and (action_type = 1 or action_type = 3) " +
                " GROUP BY vender_id,store_id,user_id,sku_id) bb " +
                " on aa.key = bb.key " +
                " group by aa.vender_id,aa.store_id ,aa.subscribe_type";

        // 推送点击下单sql
        String order_sql = " select cast(aa.vender_id AS STRING) as vender_id,cast(aa.store_id AS STRING) as store_id,count(distinct aa.user_id) as count_users ,aa.subscribe_type from " +
                " (select tt1.vender_id as vender_id,tt1.store_id as store_id,tt1.user_id as user_id,tt1.sku_id as sku_id, " +
                " concat(tt1.key,'-',tt1.sku_id) as key ,tt1.subscribe_type" +
                " from " +
                " (SELECT vender_id,store_id,user_id,concat(vender_id,'-',store_id,'-',user_id) as key " +
                " FROM dwd_data.dwd_traffic_user_events " +
                " WHERE dt = '" + dt + "' " +
                "     AND event_code = 'push_click' " +
                "     AND tdc LIKE '51.30.0.13744-15408%') tt " +
                " join " +
                " (select vender_id,store_id,sku_id,user_id,concat(vender_id,'-',store_id,'-',user_id) as key,subscribe_type " +
                " from dmall_search.search_push_list " +
                " where dt = '" + dt + "' and push_status = 4) tt1 " +
                " on tt.key = tt1.key ) aa " +
                " join " +
                " (SELECT a.vender_id AS vender_id,a.erp_store_id AS store_id,a.webuser_id AS user_id,b.sku_id AS sku_id, " +
                " concat(a.vender_id,'-',a.erp_store_id,'-',a.webuser_id,'-',b.sku_id) as key " +
                " FROM dmall_order.wm_order a " +
                " JOIN dmall_order.wm_order_ware b " +
                " ON b.order_id = a.id AND b.yn = 1 AND b.dt = '" + dt + "' AND a.dt = '" + dt + "' " +
                " WHERE a.dt = '" + dt + "' AND a.order_status = 1024 AND a.order_type = 1 AND b.sku_id != -1 " +
                " GROUP BY a.vender_id,a.erp_store_id,a.webuser_id,b.sku_id) bb " +
                " on aa.key = bb.key " +
                " group by aa.vender_id,aa.store_id ,aa.subscribe_type";
        // 查询结果rdd



        System.out.println(sub_sql);

        System.out.println(push_sql);

        System.out.println(push_reach_sql);

        System.out.println(push_click_sql);

        System.out.println(add_cart_sql);

        System.out.println(order_sql);
    }
}
