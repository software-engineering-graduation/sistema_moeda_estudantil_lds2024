import React from 'react'

interface SpinnerProps {
  size?: number
  color?: string
}

export const Spinner: React.FC<SpinnerProps> = ({ size = 8, color = 'gray-900' }) => {
  return (
    <div className="flex justify-center items-center">
      <div
        className={`animate-spin rounded-full h-${size} w-${size} border-t-2 border-b-2 border-${color}`}
      ></div>
    </div>
  )
}