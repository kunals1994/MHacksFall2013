class AddRepsToWorkouts < ActiveRecord::Migration
  def change
    add_column :workouts, :reps, :integer
  end
end
