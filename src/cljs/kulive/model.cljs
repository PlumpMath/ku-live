(ns kulive.model
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame])
  (:require-macros [reagent.ratom  :refer [reaction]]))


(def dummy-data {"DISS352-00" {:kr {:professor "이지수", :r false, :number "DISS352-00", :name " 국제법특강I(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :schedule ["화(1) 국제관322" "\n목(1) 국제관322"], :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=3928&dept_cd=3931&cour_cd=DISS352&cour_cls=00&cour_nm=국제법특강I(영강)"}, :en {:professor nil, :schedule ["Tue(1) 138-322" "\nThu(1) 138-322"], :r false, :number "DISS352-00", :name "SPECIAL TOPICS IN INTERNATIONAL LAW I(English)", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=3928&dept_cd=3931&cour_cd=DISS352&cour_cls=00&cour_nm=SPECIAL TOPICS IN INTERNATIONAL LAW I(English)"}},
                 "EDBA231-02" {:kr {:professor "박철", :r true, :number "EDBA231-02", :name " 마케팅원론", :w false, :l true, :classification "전공필수 ", :credit-hours "3(  3)", :schedule ["월(9) 33-221" "\n목(1-2) 33-325"], :x true, :campus "세종", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0293&dept_cd=4633&cour_cd=EDBA231&cour_cls=02&cour_nm=마케팅원론"}, :en {:professor "Cheol Park", :r true, :number "EDBA231-02", :name "PRINCIPLES OF MARKETING", :w false, :l true, :classification "Major Required", :credit-hours "3(  3)", :schedule ["Mon(9) 33-221" "\nThu(1-2) 33-325"], :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0293&dept_cd=4633&cour_cd=EDBA231&cour_cls=02&cour_nm=PRINCIPLES OF MARKETING"}},
                 "KFBT253-00" {:kr {:professor "김영준", :r true, :number "KFBT253-00", :name " 세포생물학", :w false, :l false, :classification "기본전공선택 ", :credit-hours "3(  3)", :schedule ["수(3-4) 7-217" "\n목(4) 9-615A"], :x false, :campus "세종", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4460&dept_cd=4554&cour_cd=KFBT253&cour_cls=00&cour_nm=세포생물학"}, :en {:professor "Young Jun Kim", :r true, :number "KFBT253-00", :name "CELL BIOLOGY", :w false, :l false, :classification "Basic Major", :credit-hours "3(  3)", :schedule ["Wed(3-4) 7-217" "\nThu(4) 9-615A"], :x false, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4460&dept_cd=4554&cour_cd=KFBT253&cour_cls=00&cour_nm=CELL BIOLOGY"}},
                 "BUSS207-03" {:kr {:professor "김우찬", :r false, :number "BUSS207-03", :name " 재무관리(영강)", :w false, :l true, :classification "전공필수 ", :credit-hours "3(  3)", :schedule ["화(1) 현차관B204" "\n목(1) 현차관B204"], :x false, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0140&dept_cd=0142&cour_cd=BUSS207&cour_cls=03&cour_nm=재무관리(영강)"}, :en {:professor "Kim, Woochan", :r false, :number "BUSS207-03", :name "FINANCIAL MANAGEMENT(English)", :w false, :l true, :classification "Major Required", :credit-hours "3(  3)", :schedule ["Tue(1) 151-B204" "\nThu(1) 151-B204"], :x false, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0140&dept_cd=0142&cour_cd=BUSS207&cour_cls=03&cour_nm=FINANCIAL MANAGEMENT(English)"}},
                 "CHEM300-01" {:kr {:professor "이광렬", :r true, :number "CHEM300-01", :name " 전공심화연구I", :w false, :l false, :classification "전공선택 ", :credit-hours "3(  6)", :schedule [], :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0209&dept_cd=0213&cour_cd=CHEM300&cour_cls=01&cour_nm=전공심화연구I"}, :en {:professor "Kwangyeol Lee", :r true, :number "CHEM300-01", :name "UNDERGRADUATE ADVANCED RESEARCH I", :w false, :l false, :classification "Major Elective", :credit-hours "3(  6)", :schedule [], :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0209&dept_cd=0213&cour_cd=CHEM300&cour_cls=01&cour_nm=UNDERGRADUATE ADVANCED RESEARCH I"}}})

;; Utils
(defn course-hm->str [[id data]]
  (str/join " "
            [(get-in data [:kr :name])
             (get-in data [:kr :number])
             (get-in data [:kr :professor])
             (apply str (get-in data [:kr :schedule]))
             (get-in data [:kr :credit-hours])
             (get-in data [:kr :classification])]))

(defn matches-query?
  [search-input course]
  (let [course-str (str/lower-case (course-hm->str course))
        search-input (str/lower-case search-input)
        search-tokenx (str/split search-input #" ")]
    (every? (fn [search-token]
              (> (.indexOf course-str search-token) -1))
            search-tokenx)))

;; Handlers (this will modify inner database)
(re-frame/register-handler
 :initialise-db
 (fn [_ _]
   {:courses dummy-data
    :search-input ""
    :my-courses []}))

(defn handle-search-input-entered
  [app-state [_ search-input]]
  (assoc-in app-state [:search-input] search-input))

(re-frame/register-handler
 :search-input-entered
 handle-search-input-entered)

(defn handle-course-search-entered
  [app-state _]
  (let [displayed-courses (re-frame/subscribe [:courses-to-display])
        search-elem (.getElementById js/document "search-courses")]
    (if (and (= 1 (count @displayed-courses))
             (not (get (set (get app-state :my-courses))
                       (ffirst @displayed-courses))))
      (do (set! (.-value search-elem) "")
          (-> app-state
              (update-in [:my-courses] conj (ffirst @displayed-courses))
              (assoc-in [:search-input] "")))
      app-state)))

(re-frame/register-handler
 :course-search-entered
 handle-course-search-entered)

;; Subscribers (this will get data based on inner database)

(re-frame/register-sub
 :search-input
 (fn [db]
   (reaction (:search-input @db))))

(re-frame/register-sub
 :courses
 (fn [db]
   (reaction (:courses @db))))

(re-frame/register-sub
 :my-courses
 (fn [db]
   (reaction (:my-courses @db))))

(re-frame/register-sub
 :not-my-courses
 (let [courses (re-frame/subscribe [:courses])
       my-courses (re-frame/subscribe [:my-courses])]
   (fn [db]
     (reaction (filterv (fn [[id course-data]]
                          (not (get (set @my-courses) id)))
                        @courses)))))

;; TODO: is it right pattern?
(re-frame/register-sub
 :filtered-courses
 (let [search-input (re-frame/subscribe [:search-input])
       not-my-courses (re-frame/subscribe [:not-my-courses])]
   (fn [_]
     (reaction
      (let [filtered-courses
            (filterv (partial matches-query? @search-input) @not-my-courses)]
        (if (and (empty? filtered-courses)
                 (> (count @search-input) 0))
          (filterv
           (partial matches-query? (apply str (butlast @search-input)))
           @not-my-courses)
          filtered-courses))))))

(re-frame/register-sub
 :courses-to-display
 (let [filtered-courses (re-frame/subscribe [:filtered-courses])]
   (fn [_]
     (reaction (take 5 @filtered-courses)))))

(re-frame/register-sub
 :count-courses-in-search
 (let [filtered (re-frame/subscribe [:filtered-courses])]
   (fn [_]
     (reaction (count @filtered)))))

;; #_(defn parse-time [vec]
;;     )

;; (let [my-courses (re-frame/subscribe [:my-courses])
;;       courses (re-frame/subscribe [:courses])]
;;   (println
;;    (for [course @my-courses]
;;      (get-in @courses [course :en :schedule]))))

