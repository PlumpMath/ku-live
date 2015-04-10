(ns kulive.core
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :as re-frame]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [cljsjs.react :as react]
            [clojure.string :as string])
  (:require-macros [reagent.ratom  :refer [reaction]])
  (:import goog.History))

;; Subscriptions & handlers -------------------------

(re-frame/register-sub
 :search-input
 (fn [db]
   (reaction (:search-input @db))))

(re-frame/register-sub
 :courses
 (fn [db]
   (reaction (:courses @db))))

#_[{:name "Beginner CSS" :number "CSS101" :time "Mon(2) Wed(2)"
    :credits "3" :type "Elective" :prof "Prof. A"}
   {:name "Intermediate CSS" :number "CSS201" :time "Tue(5) Thu(5)"
    :credits "3" :type "Elective" :prof "Prof. B"}
   {:name "Advanced Javascript" :number "JS406" :time "Mon(6) Wed(6)"
    :credits "3" :type "Major" :prof "Prof. C"}
   {:name "ClojureScript" :number "CLJS303" :time "Fri(1,2)"
    :credits "3" :type "Major" :prof "Prof. D"}
   {:name "Clojure" :number "CLJ505" :time "Tue(3,4) Thu(3,4)"
    :credits "3" :type "Major" :prof "Prof. D"}
   {:name "How to Train Your Dragon" :number "DR505" :prof "Prof. E"
    :time "Tue(7) Thu(7)" :credits "3" :type "Elective"}]

#_(def db
    (clojure.edn/read-string (slurp "resources/db/db2015-1R.edn")))

(def dummy-data {"DISS352-00" {:kr {:professor "이지수", :r false, :number "DISS352-00", :name " 국제법특강I(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :schedule ["화(1) 국제관322" "\n목(1) 국제관322"], :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=3928&dept_cd=3931&cour_cd=DISS352&cour_cls=00&cour_nm=국제법특강I(영강)"}, :en {:professor nil, :schedule ["Tue(1) 138-322" "\nThu(1) 138-322"], :r false, :number "DISS352-00", :name "SPECIAL TOPICS IN INTERNATIONAL LAW I(English)", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=3928&dept_cd=3931&cour_cd=DISS352&cour_cls=00&cour_nm=SPECIAL TOPICS IN INTERNATIONAL LAW I(English)"}},
                 "EDBA231-02" {:kr {:professor "박철", :r true, :number "EDBA231-02", :name " 마케팅원론", :w false, :l true, :classification "전공필수 ", :credit-hours "3(  3)", :schedule ["월(9) 33-221" "\n목(1-2) 33-325"], :x true, :campus "세종", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0293&dept_cd=4633&cour_cd=EDBA231&cour_cls=02&cour_nm=마케팅원론"}, :en {:professor "Cheol Park", :r true, :number "EDBA231-02", :name "PRINCIPLES OF MARKETING", :w false, :l true, :classification "Major Required", :credit-hours "3(  3)", :schedule ["Mon(9) 33-221" "\nThu(1-2) 33-325"], :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0293&dept_cd=4633&cour_cd=EDBA231&cour_cls=02&cour_nm=PRINCIPLES OF MARKETING"}},
                 "KFBT253-00" {:kr {:professor "김영준", :r true, :number "KFBT253-00", :name " 세포생물학", :w false, :l false, :classification "기본전공선택 ", :credit-hours "3(  3)", :schedule ["수(3-4) 7-217" "\n목(4) 9-615A"], :x false, :campus "세종", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4460&dept_cd=4554&cour_cd=KFBT253&cour_cls=00&cour_nm=세포생물학"}, :en {:professor "Young Jun Kim", :r true, :number "KFBT253-00", :name "CELL BIOLOGY", :w false, :l false, :classification "Basic Major", :credit-hours "3(  3)", :schedule ["Wed(3-4) 7-217" "\nThu(4) 9-615A"], :x false, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4460&dept_cd=4554&cour_cd=KFBT253&cour_cls=00&cour_nm=CELL BIOLOGY"}},
                 "BUSS207-03" {:kr {:professor "김우찬", :r false, :number "BUSS207-03", :name " 재무관리(영강)", :w false, :l true, :classification "전공필수 ", :credit-hours "3(  3)", :schedule ["화(1) 현차관B204" "\n목(1) 현차관B204"], :x false, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0140&dept_cd=0142&cour_cd=BUSS207&cour_cls=03&cour_nm=재무관리(영강)"}, :en {:professor "Kim, Woochan", :r false, :number "BUSS207-03", :name "FINANCIAL MANAGEMENT(English)", :w false, :l true, :classification "Major Required", :credit-hours "3(  3)", :schedule ["Tue(1) 151-B204" "\nThu(1) 151-B204"], :x false, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0140&dept_cd=0142&cour_cd=BUSS207&cour_cls=03&cour_nm=FINANCIAL MANAGEMENT(English)"}},
                 "CHEM300-01" {:kr {:professor "이광렬", :r true, :number "CHEM300-01", :name " 전공심화연구I", :w false, :l false, :classification "전공선택 ", :credit-hours "3(  6)", :schedule [], :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0209&dept_cd=0213&cour_cd=CHEM300&cour_cls=01&cour_nm=전공심화연구I"}, :en {:professor "Kwangyeol Lee", :r true, :number "CHEM300-01", :name "UNDERGRADUATE ADVANCED RESEARCH I", :w false, :l false, :classification "Major Elective", :credit-hours "3(  6)", :schedule [], :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0209&dept_cd=0213&cour_cd=CHEM300&cour_cls=01&cour_nm=UNDERGRADUATE ADVANCED RESEARCH I"}}})

(re-frame/register-handler
 :initialise-db
 (fn
   [_ _]
   {:courses dummy-data
    :search-input ""}))

(defn handle-search-input-entered
  [app-state [_ search-input]]
  (assoc-in app-state [:search-input] search-input))

(re-frame/register-handler
 :search-input-entered
 handle-search-input-entered)

;; Views -------------------------

(defn search-component
  []
  (let [search-input (re-frame/subscribe [:search-input])]
    (fn []
      [:form {:style {:margin-bottom "1rem"}}
       [:div.row
        [:div {:class "five columns"}
         [:label {:for "search-courses"} "Course Search"]
         [:input.u-full-width
          {:id "search-courses"
           :type "search"
           :on-change #(re-frame/dispatch
                        [:search-input-entered (-> % .-target .-value)])
           :placeholder "Course ID, Title, Professor, etc..."}]]]])))

(defn matches-query?
  [search-input course]
  (let [matches-input? (fn [key]
                         (or (re-find
                              (re-pattern (string/lower-case search-input))
                              (string/lower-case (apply str (val course))))
                             (re-find
                              (re-pattern (string/lower-case
                                           (apply str (butlast search-input))))
                              (string/lower-case (apply str (val course))))
                             ))]
    (if (= "" search-input)
      true
      (or (matches-input? :name)
          (matches-input? :number)
          (matches-input? :prof)
          (matches-input? :time)
          (matches-input? :credits)
          (matches-input? :type)
          ))))

(defn course-component
  "Individual course in search result list"
  [course]
  [:li [:a (str
            (get-in (val course) [:kr :name])
            (get-in (val course) [:kr :number])
            (get-in (val course) [:kr :professor])
            (apply str (get-in (val course) [:kr :schedule]))
            (get-in (val course) [:kr :credit-hours])
            (get-in (val course) [:kr :classification]))]])

(defn courses-component
  []
  (let [courses (re-frame/subscribe [:courses])
        search-input (re-frame/subscribe [:search-input])]
    (fn []
      [:div.row
       [:div {:class "u-full-width"}
        [:ul
         (for [course (filter (partial matches-query? @search-input) @courses)]
           ^{:key (key course)} [course-component course])]]])))

(defn course-row-component
  [course]
  [:tr
   [:td (get-in (val course) [:c-id :kr :name])]
   [:td (get-in (val course) [:c-id :kr :number])]
   [:td (get-in (val course) [:c-id :kr :professor])]
   [:td (get-in (val course) [:c-id :kr :schedule])]
   [:td (get-in (val course) [:c-id :kr :credit-hours])]
   [:td (get-in (val course) [:c-id :kr :classification])]
   ])

(defn courses-table-component
  []
  (let [courses (re-frame/subscribe [:courses])
        search-input (re-frame/subscribe [:search-input])]
    (fn []
      [:table.u-full-width
       [:thead [:tr
                [:th "Name"]
                [:th "ID"]
                [:th "Professor"]
                [:th "Schedule"]
                [:th "Credits"]
                [:th "Type"]]]
       [:tbody
        (for [course (filter (partial matches-query? @search-input) @courses)]
          ^{:key (:number course)} [course-row-component course])]])))

(defn timetable-component
  "take a list of courses, get their times, put into timetable"
  [course-ids]
  (let [courses (re-frame/subscribe [:courses])
        get-time (fn [cid] (get-in courses [cid :en :schedule]))]
    (fn []
      [:h5 "Class Schedule"
       [:table.u-full-width
        [:thead
         [:tr
          [:th] [:th "Mon"] [:th "Tue"] [:th "Wed"] [:th "Thu"] [:th "Fri"]]]
        [:tbody
         (for [time-slot (range 1 8)]
           ^{:key (hash time-slot)} [:tr
                                     [:th time-slot]
                                     [:td ""]
                                     [:td ""]
                                     [:td ""]
                                     [:td ""]
                                     [:td ""]])]]])))

(defn home-page []
  [:div.container
   [:div.row
    [:div {:course "nine column" :style {:margin-top "2%"
                                         :margin-bottom "5%"}}
     [:h2 "KU Live"]
     [search-component]
     ;; [courses-table-component]
     [courses-component]
     [timetable-component]
     [:div.row [:a {:href "#/about"} "about"]]]]])

(defn about-page []
  [:div [:a {:href "#/"} "home"]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; Routes -------------------------

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; History -------------------------

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; Initialise app -------------------------

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (re-frame/dispatch [:initialise-db])
  (mount-root))
