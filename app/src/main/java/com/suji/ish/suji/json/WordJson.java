package com.suji.ish.suji.json;

import java.util.List;

/**
 * Auto-generated: 2019-01-25 18:24:13
 */
public class WordJson {

    /**
     * word_name : go
     * is_CRI : 1
     * exchange : {"word_pl":["goes"],"word_past":["went"],"word_done":["gone"],"word_ing":["going"],"word_third":["goes"],"word_er":"","word_est":""}
     * symbols : [{"ph_en":"gəʊ","ph_am":"goʊ","ph_other":"","ph_en_mp3":"http://res.iciba.com/resource/amp3/0/0/34/d1/34d1f91fb2e514b8576fab1a75a89a6b.mp3","ph_am_mp3":"http://res.iciba.com/resource/amp3/1/0/34/d1/34d1f91fb2e514b8576fab1a75a89a6b.mp3","ph_tts_mp3":"http://res-tts.iciba.com/3/4/d/34d1f91fb2e514b8576fab1a75a89a6b.mp3","parts":[{"part":"vi.","means":["走","离开","去做","进行"]},{"part":"vt.","means":["变得","发出\u2026声音","成为","处于\u2026状态"]},{"part":"n.","means":["轮到的顺序","精力","干劲","尝试"]}]}]
     * items : [""]
     */

    private String word_name;
    private int is_CRI;
    private ExchangeBean exchange;
    private List<SymbolsBean> symbols;
    private List<String> items;

    public String getWord_name() {
        return word_name;
    }

    public void setWord_name(String word_name) {
        this.word_name = word_name;
    }

    public int getIs_CRI() {
        return is_CRI;
    }

    public void setIs_CRI(int is_CRI) {
        this.is_CRI = is_CRI;
    }

    public ExchangeBean getExchange() {
        return exchange;
    }

    public void setExchange(ExchangeBean exchange) {
        this.exchange = exchange;
    }

    public List<SymbolsBean> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<SymbolsBean> symbols) {
        this.symbols = symbols;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public static class ExchangeBean {
        /**
         * word_pl : ["goes"]
         * word_past : ["went"]
         * word_done : ["gone"]
         * word_ing : ["going"]
         * word_third : ["goes"]
         * word_er :
         * word_est :
         */

        private Object word_er;
        private Object word_est;
        private Object word_pl;
        private Object word_past;
        private Object word_done;
        private Object word_ing;
        private Object word_third;

        public Object getWord_er() {
            return word_er;
        }

        public void setWord_er(Object word_er) {
            this.word_er = word_er;
        }

        public Object getWord_est() {
            return word_est;
        }

        public void setWord_est(Object word_est) {
            this.word_est = word_est;
        }

        public Object getWord_pl() {
            return word_pl;
        }

        public void setWord_pl(Object word_pl) {
            this.word_pl = word_pl;
        }

        public Object getWord_past() {
            return word_past;
        }

        public void setWord_past(Object word_past) {
            this.word_past = word_past;
        }

        public Object getWord_done() {
            return word_done;
        }

        public void setWord_done(Object word_done) {
            this.word_done = word_done;
        }

        public Object getWord_ing() {
            return word_ing;
        }

        public void setWord_ing(Object word_ing) {
            this.word_ing = word_ing;
        }

        public Object getWord_third() {
            return word_third;
        }

        public void setWord_third(Object word_third) {
            this.word_third = word_third;
        }
    }

    public static class SymbolsBean {
        /**
         * ph_en : gəʊ
         * ph_am : goʊ
         * ph_other :
         * ph_en_mp3 : http://res.iciba.com/resource/amp3/0/0/34/d1/34d1f91fb2e514b8576fab1a75a89a6b.mp3
         * ph_am_mp3 : http://res.iciba.com/resource/amp3/1/0/34/d1/34d1f91fb2e514b8576fab1a75a89a6b.mp3
         * ph_tts_mp3 : http://res-tts.iciba.com/3/4/d/34d1f91fb2e514b8576fab1a75a89a6b.mp3
         * parts : [{"part":"vi.","means":["走","离开","去做","进行"]},{"part":"vt.","means":["变得","发出\u2026声音","成为","处于\u2026状态"]},{"part":"n.","means":["轮到的顺序","精力","干劲","尝试"]}]
         */

        private String ph_en;
        private String ph_am;
        private String ph_other;
        private String ph_en_mp3;
        private String ph_am_mp3;
        private String ph_tts_mp3;
        private List<PartsBean> parts;

        public String getPh_en() {
            return ph_en;
        }

        public void setPh_en(String ph_en) {
            this.ph_en = ph_en;
        }

        public String getPh_am() {
            return ph_am;
        }

        public void setPh_am(String ph_am) {
            this.ph_am = ph_am;
        }

        public String getPh_other() {
            return ph_other;
        }

        public void setPh_other(String ph_other) {
            this.ph_other = ph_other;
        }

        public String getPh_en_mp3() {
            return ph_en_mp3;
        }

        public void setPh_en_mp3(String ph_en_mp3) {
            this.ph_en_mp3 = ph_en_mp3;
        }

        public String getPh_am_mp3() {
            return ph_am_mp3;
        }

        public void setPh_am_mp3(String ph_am_mp3) {
            this.ph_am_mp3 = ph_am_mp3;
        }

        public String getPh_tts_mp3() {
            return ph_tts_mp3;
        }

        public void setPh_tts_mp3(String ph_tts_mp3) {
            this.ph_tts_mp3 = ph_tts_mp3;
        }

        public List<PartsBean> getParts() {
            return parts;
        }

        public void setParts(List<PartsBean> parts) {
            this.parts = parts;
        }

        public static class PartsBean {
            /**
             * part : vi.
             * means : ["走","离开","去做","进行"]
             */

            private String part;
            private List<String> means;

            public String getPart() {
                return part;
            }

            public void setPart(String part) {
                this.part = part;
            }

            public List<String> getMeans() {
                return means;
            }

            public void setMeans(List<String> means) {
                this.means = means;
            }
        }
    }
}