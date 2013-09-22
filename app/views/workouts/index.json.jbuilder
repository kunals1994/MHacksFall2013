json.array!(@workouts) do |workout|
  json.extract! workout, 
  json.url workout_url(workout, format: :json)
end
