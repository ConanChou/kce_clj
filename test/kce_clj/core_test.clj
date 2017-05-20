(ns kce-clj.core-test
  (:require [clojure.test :refer :all]
            [kce-clj.core :refer :all]))

(def books [
            ["自控力 (凯利·麦格尼格尔(Kelly McGonigal))" ["自控力" "凯利·麦格尼格尔(Kelly McGonigal)"]]
            ["天才在左疯子在右 (高铭)" ["天才在左疯子在右" "高铭"]]
            ["哲学家们都干了些什么?（2015年全新修订版） (林欣浩)" ["哲学家们都干了些什么?（2015年全新修订版）" "林欣浩"]]
            ["Dance Music Manual: Tools, Toys, and Techniques (Snoman, Rick)" ["Dance Music Manual: Tools, Toys, and Techniques" "Snoman, Rick"]]])

(def metas [
            ["- Your Bookmark on Location 444 | Added on Wednesday, May 11, 2016 10:22:15 AM" [:bookmark [:location 444 444] "Wednesday, May 11, 2016 10:22:15 AM"]]
            ["- Your Highlight on page 30 | Location 721-722 | Added on Friday, July 15, 2016 10:24:48 AM" [:highlight [:page 30 30] "Friday, July 15, 2016 10:24:48 AM"]]
            ["- Your Highlight on page 30 | Location 721-722 | Added on Friday, July 15, 2016 10:24:48 AM" [:highlight [:location 721 722] "Friday, July 15, 2016 10:24:48 AM"]]
            ["- Your Note on Location 418 | Added on Saturday, February 25, 2017 1:27:24 PM" [:note [:location 418 418] "Saturday, February 25, 2017 1:27:24 PM"]]])

(deftest title-line
  (testing "parse book title"
    (doseq [[line [title _]] books]
      (is (= title (:title (#'kce-clj.core/parse-book line))))))

  (testing "parse book author"
    (doseq [[line [_ author]] books]
      (is (= author (:author (#'kce-clj.core/parse-book line)))))))

(deftest meta-line
  (testing "parse category"
    (doseq [[line [cat _ _]] metas]
      (is (= cat (:category (#'kce-clj.core/parse-cat line))))))

  (testing "parse category"
    (doseq [[line [_ [loc_type start end] _]] metas]
      (is (= {:start start :end end} (loc_type (#'kce-clj.core/parse-meta line "EST"))))))

  (testing "parse timestamp"
    (doseq [[line [_ _ dt_str]] metas]
      (is (= (let [tz (java.util.TimeZone/getTimeZone "EST")
                   fmt (doto (java.text.SimpleDateFormat. "EEEE, MMMM d, yyyy h:m:s a")
                             (.setTimeZone tz))]
               (.parse fmt dt_str))
           (:timestamp (#'kce-clj.core/parse-meta line "EST")))))))

(deftest integrated
  (testing "parse file"
    (is (= 10 (count (extract "test/kce_clj/test_clipping.txt" "EST"))))))
