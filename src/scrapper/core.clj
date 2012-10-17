(ns scrapper.core
    (:require [net.cgrand.enlive-html :as html]
              [clojure.java.io :as io]
              [cheshire.core :as json]))

(defn page-url [page]
  (str "http://phantasytour.com/shows?band_id=4&page=" page "&tour_id=-1"))

(defn fetch-url [url]
    (html/html-resource (java.net.URL. url)))

(defn shows [page]
  (map html/text
       (html/select
         (fetch-url (page-url page))
         [:div#ajax_content :div.show_summary])))

(defn write-json
  [data page]
  (json/generate-stream data
                   (io/writer (str "pages/shows-" page ".json"))))

(defn write-page [page]
  (write-json (shows page) page))

(defn -main [& args]
  (for [page (range 1 88)] (write-page page)))
