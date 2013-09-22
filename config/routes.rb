MotionCapture::Application.routes.draw do
  root to: "workouts#index"
  resources :workouts 
end
