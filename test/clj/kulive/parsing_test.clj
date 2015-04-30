(ns kulive.parsing-test
  (:require [kulive.parsing :refer :all]
            [expectations :refer :all]))

(expect [["Tue" 1 "138-322"]]
        (parse-one-schedule "Tue(1) 138-322"))

(expect [["Thu" 1 "33-325"]]
        (parse-one-schedule "\nThu(1) 33-325"))

(expect [["Thu" 1 "33-325"] ["Thu" 2 "33-325"]]
        (parse-one-schedule "\nThu(1-2) 33-325"))

(expect [["Mon" 2 "33-325"] ["Mon" 3 "33-325"] ["Mon" 4 "33-325"]]
        (parse-one-schedule "\nMon(2-4) 33-325"))

(expect [["Tue" 9 ""] ["Tue" 10 ""]]
        (parse-one-schedule "Tue(9-10)"))

(expect [["Tue" 1 "138-322"] ["Thu" 1 "138-322"] ["Thu" 2 "138-322"]]
        (parse-schedule ["Tue(1) 138-322" "\nThu(1-2) 138-322"]))
