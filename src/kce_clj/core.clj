(ns kce-clj.core
  (:require [clojure.string :as str :refer [trim]]
            [clojure.java.io :as io :refer [reader]]))

(defn- divider?
  [s]
  (= "==========" s))

(defn- parse-book
  [book]
  (if-let [[_ title author] (re-find #"(.+)\s+\((.+)\)$" book)]
   {:title title
    :author author}
   {:title book}))

(defn- parse-cat
  [cat]
  {:category (-> (re-find #"- Your (\w+) " cat)
                 (get 1 "unknown")
                 str/lower-case
                 keyword)})

(defn- parse-loc
  [loc]
  (let [[_ type start end] (re-find #"(Location|page) (\d+)(?:-(\d+))?" loc)]
    {(keyword (str/lower-case type))
     {:start (Integer/parseInt start)
      :end (Integer/parseInt (or end start))}}))

(defn- parse-cat-loc
  [cat_loc]
  (if-let [[_ cat_page loc] (re-find #"(.+) \| (.+)" cat_loc)]
   (merge (parse-cat cat_page)
          (parse-loc cat_page)
          (parse-loc loc))
   (merge (parse-cat cat_loc)
          (parse-loc cat_loc))))

(defn- parse-dt
  [dt tz_id]
  (let [tz (java.util.TimeZone/getTimeZone tz_id)
        fmt (doto (java.text.SimpleDateFormat. "EEEE, MMMM d, yyyy h:m:s a")
                  (.setTimeZone tz))]
    {:timestamp (.parse fmt dt)}))


(defn- parse-meta
  [meta tz_id]
  (let [[cat_loc dt] (str/split meta #" \| Added on ")]
    (merge (parse-cat-loc cat_loc)
           (parse-dt dt tz_id))))

(defn- parse-entry
  [entry tz_id]
  (let [[book meta _ text] entry]
   (merge (parse-book book)
          (parse-meta meta tz_id)
          {:text text})))

(defn lazy-extract
  [rdr tz_id]
  (->> (line-seq rdr)
       (map #(str/trim %))
       (partition-by divider?)
       (remove #(divider? (first %)))
       (map #(parse-entry % tz_id))))

(defn extract
  [f tz_id]
  (with-open [rdr (io/reader f)]
    (doall (lazy-extract rdr tz_id))))
