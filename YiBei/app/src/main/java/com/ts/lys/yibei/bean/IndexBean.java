package com.ts.lys.yibei.bean;

import java.util.List;

public class IndexBean extends ErrorStatus {


    /**
     * data : {"news":[{"image":"https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png","label":"外汇 | 英镑","time":"06-11 12:44","id":0,"title":"美元。。。。。","content":"https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png"},{"image":"https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png","label":"外汇 | 英镑","time":"06-11 12:44","id":1,"title":"美元。。。。。","content":"https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png"},{"image":"https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png","label":"外汇 | 英镑","time":"06-11 12:44","id":2,"title":"美元。。。。。","content":"https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png"},{"image":"https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png","label":"外汇 | 英镑","time":"06-11 12:44","id":3,"title":"美元。。。。。","content":"https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png"},{"image":"https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png","label":"外汇 | 英镑","time":"06-11 12:44","id":4,"title":"美元。。。。。","content":"https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png"}],"hots":[{"symbol":"AUDCAD","symbolCn":"澳元日元","price":1999.23,"digits":2,"gains":0.17},{"symbol":"XAUUSD","symbolCn":"澳元日元","price":0.98723,"digits":5,"gains":-0.3},{"symbol":"AUDJPY","symbolCn":"澳元日元","price":0.98723,"digits":5,"gains":-0.45},{"symbol":"XAGUSD","symbolCn":"澳元日元","price":0.98723,"digits":5,"gains":-0.38}],"banner":[{"image":"https://forex-trading.oss-cn-hangzhou.aliyuncs.com/WWDZZ/IMG/bannerImg/05183807.png","id":0,"content":"https://baidu.com"},{"image":"https://forex-trading.oss-cn-hangzhou.aliyuncs.com/WWDZZ/IMG/bannerImg/sror60wg.png","id":1,"content":""}],"notice":[{"id":0,"title":"orzorzorzorzorz.....","content":"https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png"},{"id":1,"title":"QAQ_QAQ_QAQ_QAQ","content":""}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<NewsBean> news;
        private List<HotsBean> hots;
        private List<BannerBean> banner;
        private List<NoticeBean> notice;

        public List<NewsBean> getNews() {
            return news;
        }

        public void setNews(List<NewsBean> news) {
            this.news = news;
        }

        public List<HotsBean> getHots() {
            return hots;
        }

        public void setHots(List<HotsBean> hots) {
            this.hots = hots;
        }

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public List<NoticeBean> getNotice() {
            return notice;
        }

        public void setNotice(List<NoticeBean> notice) {
            this.notice = notice;
        }

        public static class NewsBean {
            /**
             * image : https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png
             * label : 外汇 | 英镑
             * time : 06-11 12:44
             * id : 0
             * title : 美元。。。。。
             * content : https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png
             */

            private String image;
            private String label;
            private String time;
            private int id;
            private String title;
            private String content;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

        public static class HotsBean {
            /**
             * symbol : AUDCAD
             * symbolCn : 澳元日元
             * price : 1999.23
             * digits : 2
             * gains : 0.17
             */

            private String symbol;
            private String symbolCn;
            private double price;
            private int digits;
            private double gains;
            private double yesterdayPrice;

            /**
             * -1：上一次价格更大
             * 0：两次价格相等
             * 1：这次价格更大
             */
            private int status = 0;//自定义属性

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public double getYesterdayPrice() {
                return yesterdayPrice;
            }

            public void setYesterdayPrice(double yesterdayPrice) {
                this.yesterdayPrice = yesterdayPrice;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getSymbolCn() {
                return symbolCn;
            }

            public void setSymbolCn(String symbolCn) {
                this.symbolCn = symbolCn;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getDigits() {
                return digits;
            }

            public void setDigits(int digits) {
                this.digits = digits;
            }

            public double getGains() {
                return gains;
            }

            public void setGains(double gains) {
                this.gains = gains;
            }
        }

        public static class BannerBean {
            /**
             * image : https://forex-trading.oss-cn-hangzhou.aliyuncs.com/WWDZZ/IMG/bannerImg/05183807.png
             * id : 0
             * content : https://baidu.com
             */

            private String image;
            private int id;
            private String content;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

        public static class NoticeBean {
            /**
             * id : 0
             * title : orzorzorzorzorz.....
             * content : https://forex-trading.oss-cn-hangzhou.aliyuncs.com/IMG/Article/1b9.png
             */

            private int id;
            private String title;
            private String content;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
