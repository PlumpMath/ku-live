(ns kulive.core
  (:require [kulive.model]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [clojure.string :as str])
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

#_(def db
    (clojure.edn/read-string (slurp "resources/db/db2015-1R.edn")))

(def dummy-data {"DISS352-00" {:kr {:professor "이지수", :r false, :number "DISS352-00", :name " 국제법특강I(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :shedule ["화(1) 국제관322" "\n목(1) 국제관322"], :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=3928&dept_cd=3931&cour_cd=DISS352&cour_cls=00&cour_nm=국제법특강I(영강)"}, :en {:professor nil, :schedule ["Tue(1) 138-322" "\nThu(1) 138-322"], :r false, :number "DISS352-00", :name "SPECIAL TOPICS IN INTERNATIONAL LAW I(English)", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=3928&dept_cd=3931&cour_cd=DISS352&cour_cls=00&cour_nm=SPECIAL TOPICS IN INTERNATIONAL LAW I(English)"}}, "EDBA231-02" {:kr {:professor "박철", :r true, :number "EDBA231-02", :name " 마케팅원론", :w false, :l true, :classification "전공필수 ", :credit-hours "3(  3)", :shedule ["월(9) 33-221" "\n목(1-2) 33-325"], :x true, :campus "세종", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0293&dept_cd=4633&cour_cd=EDBA231&cour_cls=02&cour_nm=마케팅원론"}, :en {:professor "Cheol Park", :r true, :number "EDBA231-02", :name "PRINCIPLES OF MARKETING", :w false, :l true, :classification "Major Required", :credit-hours "3(  3)", :shedule ["Mon(9) 33-221" "\nThu(1-2) 33-325"], :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0293&dept_cd=4633&cour_cd=EDBA231&cour_cls=02&cour_nm=PRINCIPLES OF MARKETING"}}, "KFBT253-00" {:kr {:professor "김영준", :r true, :number "KFBT253-00", :name " 세포생물학", :w false, :l false, :classification "기본전공선택 ", :credit-hours "3(  3)", :shedule ["수(3-4) 7-217" "\n목(4) 9-615A"], :x false, :campus "세종", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4460&dept_cd=4554&cour_cd=KFBT253&cour_cls=00&cour_nm=세포생물학"}, :en {:professor "Young Jun Kim", :r true, :number "KFBT253-00", :name "CELL BIOLOGY", :w false, :l false, :classification "Basic Major", :credit-hours "3(  3)", :shedule ["Wed(3-4) 7-217" "\nThu(4) 9-615A"], :x false, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4460&dept_cd=4554&cour_cd=KFBT253&cour_cls=00&cour_nm=CELL BIOLOGY"}}, "BUSS207-03" {:kr {:professor "김우찬", :r false, :number "BUSS207-03", :name " 재무관리(영강)", :w false, :l true, :classification "전공필수 ", :credit-hours "3(  3)", :shedule ["화(1) 현차관B204" "\n목(1) 현차관B204"], :x false, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0140&dept_cd=0142&cour_cd=BUSS207&cour_cls=03&cour_nm=재무관리(영강)"}, :en {:professor "Kim, Woochan", :r false, :number "BUSS207-03", :name "FINANCIAL MANAGEMENT(English)", :w false, :l true, :classification "Major Required", :credit-hours "3(  3)", :shedule ["Tue(1) 151-B204" "\nThu(1) 151-B204"], :x false, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0140&dept_cd=0142&cour_cd=BUSS207&cour_cls=03&cour_nm=FINANCIAL MANAGEMENT(English)"}}, "CHEM300-01" {:kr {:professor "이광렬", :r true, :number "CHEM300-01", :name " 전공심화연구I", :w false, :l false, :classification "전공선택 ", :credit-hours "3(  6)", :shedule [], :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0209&dept_cd=0213&cour_cd=CHEM300&cour_cls=01&cour_nm=전공심화연구I"}, :en {:professor "Kwangyeol Lee", :r true, :number "CHEM300-01", :name "UNDERGRADUATE ADVANCED RESEARCH I", :w false, :l false, :classification "Major Elective", :credit-hours "3(  6)", :shedule [], :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0209&dept_cd=0213&cour_cd=CHEM300&cour_cls=01&cour_nm=UNDERGRADUATE ADVANCED RESEARCH I"}}, "ELED241-00" {:kr {:professor "황지연", :r true, :number "ELED241-00", :name " 영미아동문학강독(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :shedule ["화(2) 사대신관 124" "\n목(2) 사대신관 124"], :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0234&dept_cd=0241&cour_cd=ELED241&cour_cls=00&cour_nm=영미아동문학강독(영강)"}, :en {:professor "Ji Yeon Hwang", :r true, :number "ELED241-00", :name "READINGS IN ENGLISH CHILDREN'S LITERATURE(English)", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :shedule ["Tue(2) 110-124" "\nThu(2) 110-124"], :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0234&dept_cd=0241&cour_cd=ELED241&cour_cls=00&cour_nm=READINGS IN ENGLISH CHILDREN'S LITERATURE(English)"}}, "ENGL421-03" {:kr {:professor "이종임", :r true, :number "ENGL421-03", :name " 영미문학특수과제Ⅰ(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :shedule ["화(5) 교양관 402" "\n목(5) 교양관 402"], :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0143&dept_cd=0146&cour_cd=ENGL421&cour_cls=03&cour_nm=영미문학특수과제Ⅰ(영강)"}, :en {:professor "Jong-Im Lee", :r true, :number "ENGL421-03", :name "SPECIAL TOPICS IN BRITISH AND AMERICAN LITERATURE(English)", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :shedule ["Tue(5) 108-402" "\nThu(5) 108-402"], :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0143&dept_cd=0146&cour_cd=ENGL421&cour_cls=03&cour_nm=SPECIAL TOPICS IN BRITISH AND AMERICAN LITERATURE(English)"}}, "COSE496-00" {:kr {:professor "한정현", :r false, :number "COSE496-00", :name " 현장실습II", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  0)", :shedule [], :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=5720&dept_cd=5722&cour_cd=COSE496&cour_cls=00&cour_nm=현장실습II"}, :en {:professor "HAN, JUNGHYUN", :r false, :number "COSE496-00", :name "INTERNSHIP PROGRAM II", :w false, :l true, :classification "Major Elective", :credit-hours "3(  0)", :shedule [], :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=5720&dept_cd=5722&cour_cd=COSE496&cour_cls=00&cour_nm=INTERNSHIP PROGRAM II"}}, "LSSS471-00" {:kr {:professor "배상우", :r false, :number "LSSS471-00", :name " 스포츠이벤트실습(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "1(  2)", :shedule ["수(8-9) 33-324"], :x false, :campus "세종", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=5685&dept_cd=5683&cour_cd=LSSS471&cour_cls=00&cour_nm=스포츠이벤트실습(영강)"}, :en {:professor "Sang Woo Bae", :r false, :number "LSSS471-00", :name "SPORT EVENT FIELDWORK(English)", :w false, :l true, :classification "Major Elective", :credit-hours "1(  2)", :shedule ["Wed(8-9) 33-324"], :x false, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=5685&dept_cd=5683&cour_cd=LSSS471&cour_cls=00&cour_nm=SPORT EVENT FIELDWORK(English)"}}, "KHSU230-00" {:kr {:professor "전옥희", :r true, :number "KHSU230-00", :name " 생화학", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :shedule ["금(1-2) 하나과학관 B117호"], :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4669&dept_cd=4893&cour_cd=KHSU230&cour_cls=00&cour_nm=생화학"}, :en {:professor "Ok-Hee Jeon", :r true, :number "KHSU230-00", :name "BIOCHEMISTRY", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :shedule ["Fri(1-2) B117"], :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4669&dept_cd=4893&cour_cd=KHSU230&cour_cls=00&cour_nm=BIOCHEMISTRY"}}})

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
  (let [search-input (re-frame/subscribe [:search-input])
        course-count (re-frame/subscribe [:count-courses-in-search])]
    (fn []
      [:form {:style {:margin-bottom "0rem"}}
       [:div.row
        [:div {:class "five columns"}
         [:label {:for "search-courses"} (str "Course Search (" @course-count ")")]
         [:input.u-full-width
          {:id "search-courses"
           :type "search"
           :on-change #(re-frame/dispatch
                        [:search-input-entered (-> % .-target .-value)])
           :placeholder "Course ID, Title, Professor, etc..."}]]]])))

(defn course-component
  "Individual course in search result list"
  [course]
  [:li [:a (kulive.model/course-hm->str course)]])

(defn courses-component []
  (let [courses (re-frame/subscribe [:filtered-courses])]
    (fn []
      [:div.row
       [:div {:class "u-full-width"}
        [:ul
         (for [course @courses]
           ^{:key (key course)} [course-component course])]]])))

(defn course-row-component [course]
  [:tr
   [:td (str (get-in (val course) [:kr :name]))]
   [:td (str (get-in (val course) [:kr :number]))]
   [:td (str (get-in (val course) [:kr :professor]))]
   [:td (apply str (get-in (val course) [:kr :schedule]))]
   [:td (str (get-in (val course) [:kr :credit-hours]))]
   [:td (str (get-in (val course) [:kr :classification]))]])

(defn courses-table-component []
  (let [courses (re-frame/subscribe [:filtered-courses])]
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
        (for [course @courses]
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
         (for [time-slot (range 1 9)]
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
  (re-frame/dispatch-sync [:initialise-db])
  (mount-root))
