class AddAvgRepTimeToWorkouts < ActiveRecord::Migration
  def change
    add_column :workouts, :avg_rep_time, :decimal
  end
end
