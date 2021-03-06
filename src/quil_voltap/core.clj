(ns quil_voltap.core
  (:require [quil.core :as q]
            [quil.middleware :as m])
  (:use [quil_voltap.voltap :as voltap]
        [overtone.live]
        [overtone.synth.stringed])
  )

(defonce sound-in-synth3000 (voltap/sound-in-vol-tap 3000))
(def circle-num 10)

(defn setup []
  (q/frame-rate 30)
  {:vol 0
   :bgcolour 0
   :radius 10})

(defn update-state [state]
  ;  (q/exit)
  (let
    [vol (voltap/sound-in-vol)
     vol3000 (voltap/sound-in-vol sound-in-synth3000)]
    {:vol vol
     :bgcolour (* 25 (* 10 vol))
     :radius (+ 10 (* 10000 vol3000))}))

(defn draw-state [state]
  (let [bgcolour (:bgcolour state)
        radius (:radius state)
        circle-num (* 5 (:vol state))
        r (+ (* 100 bgcolour) 30)
        g (+ (* 128 bgcolour) 30)
        b (+ (* 128 bgcolour) 30)
        w (q/width)
        h (q/height)
        fw (/ w 2)
        fh (/ h 2)]
    (q/background r g b)
    (dorun
      (for [i (range 0 circle-num)]
        (let [x (- (/ fw 2) (rand fw))
              y (- (/ fh 2) (rand fh))
              r (* (rand 2) radius)]
          (q/stroke-int (- 255 bgcolour))
          (q/no-fill)
          (q/with-translation [(/ w 2)
                               (/ h 2)]
            (q/ellipse x y r r)
            ))))))

(q/defsketch voltap
  :title "You spin my circle right round"
  ;;   :size :fullscreen
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
